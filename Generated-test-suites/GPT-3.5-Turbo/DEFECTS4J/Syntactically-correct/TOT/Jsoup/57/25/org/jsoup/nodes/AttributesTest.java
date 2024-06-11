import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");

        String expected = "value1";
        String actual = attributes.get("key1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetEmpty() {
        String expected = "";
        String actual = attributes.get("nonExistingKey");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("KeyA", "value1");

        String expected = "value1";
        String actual = attributes.getIgnoreCase("keyA");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseEmpty() {
        String expected = "";
        String actual = attributes.getIgnoreCase("nonExistingKey");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");

        String expected = "value1";
        String actual = attributes.get("key1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key1", true);

        boolean expectedHasKey = true;
        boolean actualHasKey = attributes.hasKey("key1");
        Assert.assertEquals(expectedHasKey, actualHasKey);

        String expectedValue = "true";
        String actualValue = attributes.get("key1");
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key1", false);

        boolean expectedHasKey = false;
        boolean actualHasKey = attributes.hasKey("key1");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);

        String expected = "value1";
        String actual = attributes.get("key1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");

        attributes.remove("key1");

        boolean expectedHasKey = false;
        boolean actualHasKey = attributes.hasKey("key1");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("KeyA", "value1");

        attributes.put("keyB", "value2");

        attributes.put("keyC", "value3");

        attributes.removeIgnoreCase("KEYB");

        boolean expectedHasKey = false;
        boolean actualHasKey = attributes.hasKeyIgnoreCase("keyB");
        Assert.assertEquals(expectedHasKey, actualHasKey);

        String expectedValue = "value1";
        String actualValue = attributes.getIgnoreCase("keyA");
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");

        boolean expectedHasKey = true;
        boolean actualHasKey = attributes.hasKey("key1");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testHasKeyFalse() {
        boolean expectedHasKey = false;
        boolean actualHasKey = attributes.hasKey("nonExistingKey");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("KeyA", "value1");

        boolean expectedHasKey = true;
        boolean actualHasKey = attributes.hasKeyIgnoreCase("keyA");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        boolean expectedHasKey = false;
        boolean actualHasKey = attributes.hasKeyIgnoreCase("nonExistingKey");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int expected = 2;
        int actual = attributes.size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        int expected = 2;
        int actual = attributes.size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        int expected = 2;
        Assert.assertEquals(expected, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int expectedSize = 2;
        List<Attribute> attributesList = attributes.asList();
        int actualSize = attributesList.size();

        Assert.assertEquals(expectedSize, actualSize);

        Attribute expectedAttribute1 = new Attribute("key1", "value1");
        Attribute expectedAttribute2 = new Attribute("key2", "value2");

        Attribute actualAttribute1 = attributesList.get(0);
        Attribute actualAttribute2 = attributesList.get(1);

        Assert.assertEquals(expectedAttribute1, actualAttribute1);
        Assert.assertEquals(expectedAttribute2, actualAttribute2);
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("key3", "value3");

        Map<String, String> dataset = attributes.dataset();

        int expectedSize = 2;
        int actualSize = dataset.size();
        Assert.assertEquals(expectedSize, actualSize);

        String expectedValue1 = "value1";
        String expectedValue2 = "value2";
        String actualValue1 = dataset.get("key1");
        String actualValue2 = dataset.get("key2");

        Assert.assertEquals(expectedValue1, actualValue1);
        Assert.assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expected = " key1=\"value1\" key2=\"value2\"";
        String actual = attributes.html();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Assert.assertEquals(attributes, attributes2);
    }

    @Test
    public void testEqualsDifferentOrder() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key1", "value1");

        Assert.assertEquals(attributes, attributes2);
    }

    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int expectedHashCode = attributes.hashCode();

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key1", "value1");

        int actualHashCode = attributes2.hashCode();

        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        Assert.assertEquals(attributes, clone);
    }

}