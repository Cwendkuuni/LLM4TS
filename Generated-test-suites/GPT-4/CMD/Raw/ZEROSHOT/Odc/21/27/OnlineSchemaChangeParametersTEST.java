
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters oscp;

    @Before
    public void setUp(){
        oscp = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError(){
        oscp.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(oscp.isContinueOnError());
        oscp.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(oscp.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters(){
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("MySQL");

        oscp.setSqlContent("SQLCONTENT");
        oscp.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        oscp.setDelimiter(";");
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> result = oscp.generateSubTaskParameters(config, "schema");
            assertFalse(result.isEmpty());
        } catch(Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("FaultyDialect");

        oscp.setSqlContent("SQLCONTENT");
        oscp.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        oscp.setDelimiter(";");
        oscp.generateSubTaskParameters(config, "schema");
    }
}
