
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        // You should mock the ConnectionConfig object and "schema" string value to pass them as arguments in the method
        ConnectionConfig connectionConfig = null;
        String schema = null; 
        // The method should throw an UnexpectedException because the connectionConfig and schema are null
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testGenerateSubTaskParametersSuccess() {
        // You should mock the ConnectionConfig object and "schema" string value to pass them as arguments in the method
        ConnectionConfig connectionConfig = null; // Mock object
        String schema = null; // Mock object
        // You should also mock the behavior of "SqlUtils.split", "SubTaskParameterFactory.generate", 
        // "DdlUtils.replaceTableName" to make the method works properly.
        // The test case is assuming you have mocked necessary objects and methods.
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        // based on your actual requirement to assert the "subTaskParameters"
        Assert.assertNotNull(subTaskParameters);
        Assert.assertEquals(expectedSize, subTaskParameters.size()); // you should replace expectedSize with your actual expected size
    }
}
