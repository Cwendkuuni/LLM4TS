import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.constant.SwapTableType;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeSqlType;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE table1 (id INT PRIMARY KEY) ENGINE=OBSERVER; ALTER TABLE table1 ADD COLUMN column1 INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW);
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParameter = taskParameters.get(0);
        assertEquals("CREATE TABLE table1 (id INT PRIMARY KEY) ENGINE=OBSERVER;", firstTaskParameter.getSqlsToBeExecuted().get(0));
        assertEquals("ALTER TABLE table1 ADD COLUMN column1 INT;", firstTaskParameter.getSqlsToBeExecuted().get(1));
        assertEquals("testSchema", firstTaskParameter.getDatabaseName());
        assertEquals("table1", firstTaskParameter.getOriginTableName());
        assertEquals("table1", firstTaskParameter.getNewTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, firstTaskParameter.getSqlType());

        OnlineSchemaChangeScheduleTaskParameters secondTaskParameter = taskParameters.get(1);
        assertEquals("testSchema", secondTaskParameter.getDatabaseName());
        assertNull(secondTaskParameter.getOriginTableName());
        assertNull(secondTaskParameter.getNewTableName());
        assertNull(secondTaskParameter.getSqlsToBeExecuted());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws UnexpectedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "ALTER TABLE table1 ADD COLUMN column1 INT; CREATE TABLE table2 (id INT PRIMARY KEY) ENGINE=OBSERVER;";
        parameters.setSqlContent(sqlContent);
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testGetSqlsToBeExecuted() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        List<String> sqlsToBeExecuted = taskParameters.get(0).getSqlsToBeExecuted();
        List<String> expectedSqls = Arrays.asList(
                "CREATE TABLE table1 (id INT PRIMARY KEY) ENGINE=OBSERVER;",
                "ALTER TABLE table1 ADD COLUMN column1 INT;"
        );
        assertEquals(expectedSqls, sqlsToBeExecuted);
    }

    @Test
    public void testOriginTableCleanStrategy() {
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testLockUsers() {
        parameters.setLockUsers(new ArrayList<String>() {{
            add("user1");
            add("user2");
        }});
        assertEquals(2, parameters.getLockUsers().size());
        assertEquals("user1", parameters.getLockUsers().get(0));
        assertEquals("user2", parameters.getLockUsers().get(1));
    }
}