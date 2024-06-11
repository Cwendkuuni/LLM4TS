import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain() {
        String[] args = new String[]{"property_file"};
        LagoonCLI.main(args);
    }

    @Test
    public void testGetProperty() {
        String property = null;
        try {
            property = LagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            e.printStackTrace();
        }
        assert property != null;
        assert property.equals("targetURL_value");
    }

    @Test
    public void testShowTime() {
        LagoonCLI.showTime(5000L);
    }
}