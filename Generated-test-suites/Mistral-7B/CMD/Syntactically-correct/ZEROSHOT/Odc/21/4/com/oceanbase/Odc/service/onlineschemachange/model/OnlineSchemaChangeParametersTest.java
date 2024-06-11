import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryImpl;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlContent());
        assertFalse(parameters.isContinueOnError());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertEquals(TaskErrorStrategy.DEFAULT, parameters.getErrorStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertSame(OnlineSchemaChangeSqlType.DEFAULT, parameters.getSqlType());
        assertNull(parameters.getSwapTableType());

        OnlineSchemaChangeParameters clone = new OnlineSchemaChangeParameters(parameters);
        assertEquals(parameters, clone);
        assertSame(parameters.hashCode(), clone.hashCode());
    }

    @Test
    public void testSetters() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());

        parameters.setSqlContent("DROP TABLE t;");
        assertEquals("DROP TABLE t;\n", parameters.getSqlContent());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setLockTableTimeOutSeconds(30);
        assertEquals(30, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());

        parameters.setDelimiter(";|");
        assertEquals(";|", parameters.getDelimiter());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        parameters.setSwapTableType(SwapTableType.TO_NEW_TABLE);
        assertEquals(SwapTableType.TO_NEW_TABLE, parameters.getSwapTableType());

        parameters.setSqlContent("ALTER TABLE t RENAME TO t1;\nALTER TABLE t RENAME TO t2;\n");
        List<String> sqls = Arrays.asList("ALTER TABLE t RENAME TO t1;", "ALTER TABLE t RENAME TO t2;");
        assertEquals(sqls, SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(), ";"));
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String schema = "test";
        parameters.setSqlContent("ALTER TABLE t RENAME TO t1;\nALTER TABLE t RENAME TO t2;\n");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        SubTaskParameterFactory factory = new SubTaskParameterFactoryImpl();
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParam1 = subTaskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters taskParam2 = subTaskParameters.get(1);

        assertEquals("test", taskParam1.getDatabaseName());
        assertEquals("t", taskParam1.getOriginTableName());
        assertEquals("t1", taskParam1.getNewTableName());
        assertEquals(Arrays.asList("ALTER TABLE t RENAME TO t1;"), taskParam1.getSqlsToBeExecuted());

        assertEquals("test", taskParam2.getDatabaseName());
        assertEquals("t", taskParam2.getOriginTableName());
        assertEquals("t2", taskParam2.getNewTableName());
        assertEquals(Arrays.asList("ALTER TABLE t RENAME TO t2;"), taskParam2.getSqlsToBeExecuted());

        String newAlterStmt1 = DdlUtils.replaceTableName("ALTER TABLE t RENAME TO t1;", "t1", connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        String newAlterStmt2 = DdlUtils.replaceTableName("ALTER TABLE t RENAME TO t2;", "t2", connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals(Arrays.asList(newAlterStmt1, newAlterStmt2), taskParam1.getAdditionalSqlsToBeExecuted());
        assertEquals(Arrays.asList(), taskParam2.getAdditionalSqlsToBeExecuted());

        assertEquals(TaskErrorStrategy.DEFAULT, taskParam1.getErrorStrategy());
        assertEquals(TaskErrorStrategy.DEFAULT, taskParam2.getErrorStrategy());
        assertEquals(1, taskParam1.getLockTableTimeOutSeconds().intValue());
        assertEquals(1, taskParam2.getLockTableTimeOutSeconds().intValue());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParam1.getErrorStrategyOnError());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParam2.getErrorStrategyOnError());
        assertEquals(OriginTableCleanStrategy.NO_CLEAN, taskParam1.getOriginTableCleanStrategy());
        assertEquals(OriginTableCleanStrategy.NO_CLEAN, taskParam2.getOriginTableCleanStrategy());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_UnexpectedException() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String schema = "test";
        parameters.setSqlContent("ALTER TABLE t RENAME TO t1;\nALTER TABLE t RENAME TO t2;\n");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        try (SubTaskParameterFactory factory = new SubTaskParameterFactory(connectionConfig, schema)) {
            factory.generate(null, OnlineSchemaChangeSqlType.ALTER);
        }
    }
}