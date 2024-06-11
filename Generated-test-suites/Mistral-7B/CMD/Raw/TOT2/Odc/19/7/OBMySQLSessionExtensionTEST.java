package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.TestUtils;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@Slf4j
public class OBMySQLSessionExtensionTest {

 private Connection testConnection;
 private Connection testOceanBaseConnection;

 @Before
 public void setup() throws Exception {
 testConnection = TestUtils.getTestConnection();
 testOceanBaseConnection = TestUtils.getTestOceanBaseConnection();
 }

 // Add other test methods here

}