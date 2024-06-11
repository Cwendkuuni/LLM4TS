package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp(){
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test MODIFY column column1 INT;");
        connectionConfig = new ConnectionConfig();
        // setting up the connection, user needs to provide actual values
        connectionConfig.setDialectType("MySQL");
        connectionConfig.setUserName("userName");
        connectionConfig.setUserPassword("userPassword");
        connectionConfig.setDatabaseName("databaseName");
        connectionConfig.setServerHost("serverHost");
        connectionConfig.setServerPort(3306);
        
    }

    @Test
    public void isContinueOnErrorTest() {
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void generateSubTaskParametersTest() {
        
        // Assuming one subtask is generated
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,"schema");
        assertEquals(1,subTaskParameters.size());
        
        // Assuming a null connectionConfig is supplied
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(null, "schema");
            fail("Exception was expected for null connectionConfig.");
        } catch (Exception ex){
            // An exception is expected to be thrown
        }
    }
}
