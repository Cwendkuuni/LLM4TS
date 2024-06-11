package com.oceanbase.odc.service.onlineschemachange.model.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryTest;

public class OnlineSchemaChangeParametersTest {

 private ConnectionConfig connectionConfig;
 private SubTaskParameterFactory subTaskParameterFactory;

 @Before
 public void setUp() {
 connectionConfig = new ConnectionConfig();
 subTaskParameterFactory = mock(SubTaskParameterFactory.class);
 }

 // Expert 1's test cases
 @Test
 public void testConstructorAndGetterSetter() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 assertNull(parameters.getSqlContent());
 assertNull(parameters.getErrorStrategy());
 // set values and check getters
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("test sql content;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 // check getters
 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
 assertEquals("test sql content;", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 assertFalse(parameters.isContinueOnError());
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParametersNormalScenario() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("create table test_table (id int);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertFalse(result.isEmpty());
 OnlineSchemaChangeScheduleTaskParameters taskParameters = result.get(0);
 assertTrue(taskParameters.getDatabaseName().equals("test_schema"));
 assertEquals("create table test_table (id int);", taskParameters.getSqlsToBeExecuted().get(0));
 }

 // Expert 2's test cases
 @Test(expected = NullPointerException.class)
 public void testConstructorNullInput() {
 new OnlineSchemaChangeParameters(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorInvalidSqlType() {
 new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.INVALID);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptySqlContent() {
 new OnlineSchemaChangeParameters("", OnlineSchemaChangeSqlType.ALTER);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorInvalidErrorStrategy() {
 new OnlineSchemaChangeParameters(TaskErrorStrategy.INVALID, "test sql content;", OnlineSchemaChangeSqlType.ALTER);
 }

 @Test
 public void testGenerateSubTaskParametersEmptyList() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters("test sql content;", OnlineSchemaChangeSqlType.ALTER);
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertTrue(result.isEmpty());
 }

 // Expert 3's test cases
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters("test sql content;", OnlineSchemaChangeSqlType.ALTER);
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
         .thenThrow(new RuntimeException("test exception"));
 try {
 parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("Expected UnexpectedException");
 } catch (UnexpectedException e) {
 // pass
 }
 }

}