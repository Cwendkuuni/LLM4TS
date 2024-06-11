import org.junit.Test;

public class LagoonCLITest {
   
   @Test
   public void test_main(){
      String[] args = {"property_file", "interval"};
      LagoonCLI.main(args);
   }
   
   @Test
   public void test_getProperty() throws LagoonException {
      String name = "targetURL";
      String value = LagoonCLI.getProperty(name);
      assertNotNull(value);
   }
   
   @Test
   public void test_showTime() {
      long ms = 5000L;
      LagoonCLI.showTime(ms);
   }
   
}