import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.online.schema.change.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.online.schema.change.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.online.schema.change.model.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGetSqlType() {
        OnlineSchemaChangeSqlType expected = OnlineSchemaChangeSqlType.CREATE;
        OnlineSchemaChangeSqlType actual = onlineSchemaChangeParameters.getSqlType();

        onlineSchemaChangeParameters.setSqlType(expected);

        assertEquals(expected, actual);
    }

    @Test
    public void testSetAndGetSqlContent() {
        String expected = "CREATE TABLE test_table (id INT PRIMARY KEY)";
        onlineSchemaChangeParameters.setSqlContent(expected);

        String actual = onlineSchemaChangeParameters.getSqlContent();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsContinueOnError() {
        TaskErrorStrategy expected = TaskErrorStrategy.CONTINUE;
        onlineSchemaChangeParameters.setErrorStrategy(expected);

        boolean actual = onlineSchemaChangeParameters.isContinueOnError();

        assertEquals(expected, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(expected == TaskErrorStrategy.CONTINUE, actual);
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        int expected = 30;
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(expected);

        int actual = onlineSchemaChangeParameters.getLockTableTimeOutSeconds();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        int expected = 5;
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(expected);

        int actual = onlineSchemaChangeParameters.getSwapTableNameRetryTimes();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters.OriginTableCleanStrategy expected = OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP;
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(expected);

        OnlineSchemaChangeParameters.OriginTableCleanStrategy actual = onlineSchemaChangeParameters.getOriginTableCleanStrategy();

        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSubTaskParameters_EmptySqlContent() {
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        onlineSchemaChangeParameters.setSqlContent("");

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
            List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

            assertEquals(expected.size(), actual.size());
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParameters_ValidSqlContent() {
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN col INT";
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_ALTER);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
            List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

            assertEquals(expected.size(), actual.size());
            for (OnlineSchemaChangeScheduleTaskParameters expectedParameter : expected) {
                OnlineSchemaChangeScheduleTaskParameters actualParameter = actual.get(0);
                assertEquals(expectedParameter.getDatabaseName(), actualParameter.getDatabaseName());
                assertEquals(expectedParameter.getOriginTableName(), actualParameter.getOriginTableName());
                assertEquals(expectedParameter.getNewTableName(), actualParameter.getNewTableName());
                assertEquals(expectedParameter.getSqlsToBeExecuted().size(), actualParameter.getSqlsToBeExecuted().size());
                assertEquals(expectedParameter.getSqlsToBeExecuted().get(0), actualParameter.getSqlsToBeExecuted().get(0));
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Exception() {
        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException());

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e) {
            // Expected exception
        }
    }
}