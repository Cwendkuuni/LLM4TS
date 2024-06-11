package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
        // Setting default values
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("Alter statement");
        
        connectionConfig.setDialectType(DialectType.SQLServer);
        // Set other required fields for connectionConfig
    }

    @Test
    public void isContinueOnErrorTest() {
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    // Check edge conditions where no parameters was set for generateSubTaskParameters
    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersWithoutRequiredFieldsTest() {
        parameters.setSqlContent(null);
        parameters.generateSubTaskParameters(connectionConfig, "Schema1");
    }

    // Check behavior when sqlContent is set but sqlType was not set
    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersWithoutSqlTypeTest() {
        parameters.setSqlType(null);
        parameters.generateSubTaskParameters(connectionConfig, "Schema1");
    }

    // Check behavior when valid parameters was set for generateSubTaskParameters
    // Additional mocking might be required for testing this method accurately
    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersWithValidParametersTest() {
        parameters.generateSubTaskParameters(connectionConfig, "Schema1");
    }
}