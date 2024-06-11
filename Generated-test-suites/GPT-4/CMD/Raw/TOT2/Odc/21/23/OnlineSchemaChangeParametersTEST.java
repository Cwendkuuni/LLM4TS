
import static org.junit.Assert.*;
import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig config = new ConnectionConfig();

        params.setSqlType(OnlineSchemaChangeSqlType.ADD);
        params.setSqlContent("CREATE TABLE TEST_TABLE (ID INT PRIMARY KEY);");
        params.setDelimiter(";");
        params.setSwapTableNameRetryTimes(2);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.AT_ONCE);

        // Check with a valid situation
        try {
            assertNotNull(params.generateSubTaskParameters(config, "test_schema"));
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        // Check for situations that should trigger an exception
        params.setSqlContent(null);
        try {
            params.generateSubTaskParameters(config, "test_schema");
            fail("Expected an UnexpectedException to be thrown");
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: " + params, e.getMessage());
        }
    }
    
    @Test
    public void testLockUserList(){
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        List<String> lockUsers = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
        params.setLockUsers(lockUsers);
        assertEquals(params.getLockUsers(), lockUsers);
        
        // Test for empty list
        params.setLockUsers(new ArrayList<>());
        assertEquals(params.getLockUsers(), new ArrayList<>());
        
        // Test for null list
        params.setLockUsers(null);
        assertNull(params.getLockUsers());
    }
}
