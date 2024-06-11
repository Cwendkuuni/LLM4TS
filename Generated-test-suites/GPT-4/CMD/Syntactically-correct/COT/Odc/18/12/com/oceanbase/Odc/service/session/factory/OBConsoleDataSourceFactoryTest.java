package com.oceanbase.odc.service.session.factory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory.ValueMapper;
import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;


public class OBConsoleDataSourceFactoryTest {

   private static final ConnectionConfig DUMMY_CONFIG = new ConnectionConfig();

   @Test(expected = NullPointerException.class)
   public void testConstructorWithNullConfig() {
       new OBConsoleDataSourceFactory(null, true);
   }
   
   @Test
   public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
        Assert.assertNotNull(dataSourceFactory.getJdbcUrl());
   }
   
   @Test
   public void testGetUsername() {
       OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
       Assert.assertNotNull(dataSourceFactory.getUsername());
   }

   @Test
   public void testGetPassword() {
       OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
       Assert.assertNotNull(dataSourceFactory.getPassword());
   }

   @Test
   public void testGetConnectType() {
       OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
       Assert.assertNotNull(dataSourceFactory.getConnectType());
   }

   @Test
   public void testResetUsernameWithNull() {
       OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
       dataSourceFactory.resetUsername(null);
       Assert.assertNotNull(dataSourceFactory.getUsername());
   }

   @Test
   public void testResetPasswordWithNull() {
       OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
       dataSourceFactory.resetPassword(null);
       Assert.assertNotNull(dataSourceFactory.getPassword());
   }


   @Test(expected = IllegalArgumentException.class)
   public void testResetPortWithNegative() {
       OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(DUMMY_CONFIG, true);
       ValueMapper<Integer> negativeValueMapper = oldValue -> -1;
       dataSourceFactory.resetPort(negativeValueMapper);
   }

}