import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.model.ConnectionConfig;
import com.oceanbase.odc.core.model.TaskErrorStrategy;
import com.oceanbase.odc.core.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("sql_type");
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("sqlContent;");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.OLD_FOR_NEW);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("sqlContent;", onlineSchemaChangeParameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(10, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.OLD_FOR_NEW, onlineSchemaChangeParameters.getSwapTableType());
        assertNull(onlineSchemaChangeParameters.getLockUsers());
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
        assertSame(connectionConfig, onlineSchemaChangeParameters.getConnectionConfig());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parametersWithContinue = new OnlineSchemaChangeParameters();
        parametersWithContinue.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parametersWithContinue.isContinueOnError());

        OnlineSchemaChangeParameters parametersWithoutContinue = new OnlineSchemaChangeParameters();
        assertFalse(parametersWithoutContinue.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        List<String> expectedSqls = Arrays.asList(
                "sqlContent1;", "sqlContent2;");

        List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, actualSubTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParameters = actualSubTaskParameters.get(0);
        TableIdentity firstTableIdentity = new TableIdentity("databaseName1", "tableName1");
        assertEquals(firstTableIdentity, firstTaskParameters.getTableIdentity());
        assertEquals("sqlContent1;", firstTaskParameters.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters secondTaskParameters = actualSubTaskParameters.get(1);
        TableIdentity secondTableIdentity = new TableIdentity("databaseName2", "tableName2");
        assertEquals(secondTableIdentity, secondTaskParameters.getTableIdentity());
        assertEquals("sqlContent2;", secondTaskParameters.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String newTableName = "newTableName";
        String alteredStmt = DdlUtils.replaceTableName("sqlContent1;", newTableName, "sql_type", sqlType);
        assertEquals(Arrays.asList(alteredStmt), firstTaskParameters.getSqlsToBeExecuted());
    }
}