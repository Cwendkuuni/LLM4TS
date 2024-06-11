
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        parameters.setSqlContent("Create table Test;");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.SQL);
        
        Assert.assertEquals(new ArrayList<>(), parameters.generateSubTaskParameters(connectionConfig, "schema"));
    }

    @Test
    public void testSetGetSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        parameters.setSwapTableType(SwapTableType.SOME_TYPE);
        Assert.assertEquals(SwapTableType.SOME_TYPE, parameters.getSwapTableType());
    }

    // similarly for all the other fields
}
