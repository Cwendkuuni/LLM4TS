import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import java.util.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assert attributes.get("key1").equals("value1");
        assert attributes.get("key2").equals("value2");
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assert attributes.get("key1").equals("value1");

        attributes.put("key2", "value2");
        assert attributes.get("key2").equals("value2");
    }

    // Add more test cases for other methods as needed

}