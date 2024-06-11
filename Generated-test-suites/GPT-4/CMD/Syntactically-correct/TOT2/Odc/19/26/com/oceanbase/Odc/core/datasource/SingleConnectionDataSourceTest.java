package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

   private SingleConnectionDataSource singleConnectionDataSource;

   @Before
   public void setUp() throws Exception {
      singleConnectionDataSource = new SingleConnectionDataSource();
   } 

   @Test
   public void testMethod() throws Exception {
      // Arrange
      String input = "testinput";
      
      // Act
      String result = singleConnectionDataSource.method(input);

      //Assert 
      assertEquals("Expected result", result);
   }
}