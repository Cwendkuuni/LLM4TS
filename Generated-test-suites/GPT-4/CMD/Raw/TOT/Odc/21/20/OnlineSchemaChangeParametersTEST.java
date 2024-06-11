import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {

        //simulating input to create connection configuration
        DialectType dialectType = new DialectType();
        dialectType.setDialectType("MySQL-8.0.17");
        ConnectionConfig connConfig = new ConnectionConfig();
        connConfig.setDialectType(dialectType);

        //simulating OnlineSchemaChange SqlType to ALTER
        OnlineSchemaChangeSqlType sqlType = new OnlineSchemaChangeSqlType();
        sqlType.setOnlineSchemaChangeSqlType("ALTER");

        //simulating input to create OnlineSchemaChangeParameters
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(sqlType);
        parameters.setSqlContent("ALTER TABLE");

        //Executing method with generated data
        parameters.generateSubTaskParameters(connConfig, "testSchema");
    }
}