package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

	@Test
	public void testIsContinueOnError() {
		OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
		params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
		assertTrue(params.isContinueOnError());
	}

	@Test
	public void testGenerateSubTaskParameters() {
		OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
		params.setSqlContent("CREATE TABLE test_table (id INT, name VARCHAR(255));");
		params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
		params.setDelimiter(";");
		List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(new ConnectionConfig(), "test_schema");
		assertNotNull(subTaskParams);
		assertEquals(subTaskParams.size(), 1);
	}

}