package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;
import org.junit.Assert;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        parameters.setSqlContent("CREATE TABLE myTable;");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subParameters = parameters.generateSubTaskParameters(connectionConfig, "mySchema");
            OnlineSchemaChangeScheduleTaskParameters subParam = subParameters.get(0);
            
            Assert.assertEquals(1, subParameters.size());
            Assert.assertEquals("mySchema", subParam.getDatabaseName());
            Assert.assertEquals("myTable", subParam.getOriginTableName());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown while generating subTaskParameters: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithAlterSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        parameters.setSqlContent("ALTER TABLE myTable ADD COLUMN myColumn INT;");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subParameters = parameters.generateSubTaskParameters(connectionConfig, "mySchema");
            OnlineSchemaChangeScheduleTaskParameters subParam = subParameters.get(0);

            Assert.assertEquals(1, subParameters.size());
            Assert.assertEquals("mySchema", subParam.getDatabaseName());
            Assert.assertEquals("myTable", subParam.getOriginTableName());
            Assert.assertTrue(subParam.getSqlsToBeExecuted().get(0).contains("ADD COLUMN myColumn INT"));
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown while generating subTaskParameters: " + e.getMessage());
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersThrowsException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();

        parameters.setSqlContent("CREATE TABLE myTable;");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        // This should throw exception as dialectType is not set in connectionConfig
        parameters.generateSubTaskParameters(connectionConfig, "mySchema");
    }
}