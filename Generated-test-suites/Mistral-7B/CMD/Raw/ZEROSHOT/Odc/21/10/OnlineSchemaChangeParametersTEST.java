package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

/**
 * Unit tests for OnlineSchemaChangeParameters class
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        assertNotNull(onlineSchemaChangeParameters);
    }

    @Test
    public void testGettersAndSetters() {
        // test setting properties
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW_TABLE);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));

        // test getting properties
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;\n", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP_TO_NEW_TABLE, parameters.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());

        // test isContinueOnError
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.HALT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        // given
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.HALT);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW_TABLE);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));

        // when
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_schema");

        // then
        assertEquals(1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        assertEquals("test_schema", taskParameter.getDatabaseName());
        assertEquals("test_table", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;\n", taskParameter.getSqlContent());
        assertEquals(TaskErrorStrategy.HALT, taskParameter.getErrorStrategy());
        assertEquals(30, taskParameter.getLockTableTimeOutSeconds());
        assertNull(taskParameter.getNewTableName());
        assertEquals(Arrays.asList("ALTER TABLE test_table ADD COLUMN new_column INT;\n"), taskParameter.getSqlsToBeExecuted());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        // given
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.HALT);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW_TABLE);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));

        // when
        String invalidSqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT; DROP TABLE test_schema.test_table;";
        parameters.setSqlContent(invalidSqlContent);

        // then (should throw an exception)
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_schema");
    }
}