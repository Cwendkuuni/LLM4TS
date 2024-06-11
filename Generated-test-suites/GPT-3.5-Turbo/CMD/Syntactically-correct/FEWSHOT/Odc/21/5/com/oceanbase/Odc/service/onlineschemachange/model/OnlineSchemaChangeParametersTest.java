import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name VARCHAR(255);");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(60);
        params.setSwapTableNameRetryTimes(3);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.RENAME);
        params.setDelimiter(";");

        assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
        assertEquals("ALTER TABLE table_name ADD COLUMN column_name VARCHAR(255);", params.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
        assertEquals(60, params.getLockTableTimeOutSeconds().intValue());
        assertEquals(3, params.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.RENAME, params.getOriginTableCleanStrategy());
        assertEquals(";", params.getDelimiter());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name VARCHAR(255);");
        params.setDelimiter(";");

        // Mock connectionConfig and schema
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "public";

        assertNotNull(params.generateSubTaskParameters(connectionConfig, schema));
    }
}