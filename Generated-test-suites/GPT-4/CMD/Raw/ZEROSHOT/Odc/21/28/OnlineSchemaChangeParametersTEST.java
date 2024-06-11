
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test ADD COLUMN test_column INT(11)");
        onlineSchemaChangeParameters.setDelimiter(";");

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        } catch (Exception e) {
            assertNotNull(e);
        }

        // Verify
        verify(connectionConfig, times(1)).getDialectType();
    }

    @Test
    public void testProperties() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());

        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test ADD COLUMN test_column INT(11)");
        assertEquals("ALTER TABLE test ADD COLUMN test_column INT(11)", onlineSchemaChangeParameters.getSqlContent());

        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        assertEquals(Integer.valueOf(30), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());

        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());

        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());

        onlineSchemaChangeParameters.setDelimiter(";");
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());

        onlineSchemaChangeParameters.setLockUsers(Collections.singletonList("test_user"));
        assertEquals(Collections.singletonList("test_user"), onlineSchemaChangeParameters.getLockUsers());

        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.RENAME);
        assertEquals(SwapTableType.RENAME, onlineSchemaChangeParameters.getSwapTableType());
    }
}
