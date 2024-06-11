package com.oceanbase.odc.service.onlineschemachange.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;

public class OnlineSchemaChangeParametersTest {

 // Test Case 1: Default constructor test
 @Test
 public void testDefaultConstructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 assertNull(parameters.getSqlType());
 assertNull(parameters.getSqlContent());
 assertNull(parameters.getErrorStrategy());
 assertNull(parameters.getLockTableTimeOutSeconds());
 assertNull(parameters.getSwapTableNameRetryTimes());
 assertNull(parameters.getOriginTableCleanStrategy());
 assertNull(parameters.getDelimiter());
 assertNull(parameters.getLockUsers());
 assertNull(parameters.getSwapTableType());
 }

 // Test Case 2: Constructor with all arguments
 @Test
 public void testConstructorWithAllArguments() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
 OnlineSchemaChangeSqlType.CREATE,
 "CREATE TABLE test_table (id INT PRIMARY KEY);",
 TaskErrorStrategy.CONTINUE,
 15,
 3,
 OriginTableCleanStrategy.DROP_OLD,
 ";",
 Arrays.asList("user1", "user2"),
 SwapTableType.SWAP);

 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY);", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(15, parameters.getLockTableTimeOutSeconds());
 assertEquals(3, parameters.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.DROP_OLD, parameters.getOriginTableCleanStrategy());
 assertEquals(";", parameters.getDelimiter());
 assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
 assertEquals(SwapTableType.SWAP, parameters.getSwapTableType());
 }

 // Test Case 3: Getters and Setters
 @Test
 public void testGettersAndSetters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.RENAME);
 parameters.setDelimiter("\\r\\n");
 parameters.setLockUsers(Collections.singletonList("user"));
 parameters.setSwapTableType(SwapTableType.REPLACE);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
 assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(5, parameters.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.RENAME, parameters.getOriginTableCleanStrategy());
 assertEquals("\\r\\n", parameters.getDelimiter());
 assertEquals(Collections.singletonList("user"), parameters.getLockUsers());
 assertEquals(SwapTableType.REPLACE, parameters.getSwapTableType());
 }

 // Test Case 4: isContinueOnError() method test
 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(parameters.isContinueOnError());
 }

 // Test Case 5: generateSubTaskParameters() method test - normal scenario
 @Test
 public void testGenerateSubTaskParametersNormalScenario() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
 OnlineSchemaChangeSqlType.CREATE,
 "CREATE TABLE test_table (id INT PRIMARY KEY);",
 TaskErrorStrategy.CONTINUE,
 15,
 3,
 OriginTableCleanStrategy.DROP_OLD,
 ";",
 Arrays.asList("user1", "user2"),
 SwapTableType.SWAP);

 List<String> expectedSqls = Arrays.asList(
 "CREATE TABLE test_table (id INT PRIMARY KEY);",
 "ALTER TABLE test_table RENAME TO test_table_new;",
 "DROP TABLE test_table;");

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 List<String> resultSqls = new ArrayList<>();
 for (OnlineSchemaChangeScheduleTaskParameters taskParameter : result) {
 resultSqls.addAll(taskParameter.getSqlsToBeExecuted());
 }
 assertEquals(expectedSqls, resultSqls);
 }

 // Test Case 6: generateSubTaskParameters() method test - exception scenario
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersExceptionScenario() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
 OnlineSchemaChangeSqlType.CREATE,
 "CREATE TABLE test_table (id INT PRIMARY KEY);",
 TaskErrorStrategy.CONTINUE,
 15,
 3,
 OriginTableCleanStrategy.DROP_OLD,
 ";",
 Arrays.asList("user1", "user2"),
 SwapTableType.SWAP);

 // Change the sqlContent to cause an exception
 parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY) THROW EXCEPTION;");
 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 }
}