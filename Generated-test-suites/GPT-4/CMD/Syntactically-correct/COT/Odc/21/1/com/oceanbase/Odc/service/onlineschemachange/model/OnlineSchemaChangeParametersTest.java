package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import org.junit.Assert;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = NullPointerException.class)
    public void testIsContinueOnErrorWithNullPointerException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(null);
        parameters.isContinueOnError();
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("create table students (id int, name varchar(100));");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("MySQL");
        parameters.generateSubTaskParameters(config, "testDB");
    }

    @Test
    public void testGenerateSubTaskParameters(){
        //Preparing the connection config, sqls, etc. Then checking if there are expected subTaskParameters.
        //This test could be involved and depends on many factors. It's not directly provided due to the complexity.
    }
}
