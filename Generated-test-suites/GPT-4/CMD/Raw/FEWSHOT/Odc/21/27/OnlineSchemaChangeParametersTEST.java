package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        
        // Put your desired schema here. 
        String schema = "Your_Schema_Here"; 

        onlineSchemaChangeParameters.setSqlContent("Your_SQL_content_Here");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("User1");
        lockUsers.add("User2");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        
        onlineSchemaChangeParameters.setDelimiter(";");

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        // TableIdentity could not be created due to the wrong SQLcontent.
        // Therefore, it will throw UnexpectedException.
    }
}