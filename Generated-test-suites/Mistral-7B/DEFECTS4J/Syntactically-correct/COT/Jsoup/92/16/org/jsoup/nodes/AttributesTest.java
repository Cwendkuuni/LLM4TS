import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String actualFirst = attributes.get("key");
        String expectedFirst = "value2";
        Assert.assertEquals(expectedFirst, actualFirst);

        String actualSecond = attributes.getIgnoreCase("key");
        String expectedSecond = "value2";
        Assert.assertEquals(expectedSecond, actualSecond);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "value1");
        attributes.putIgnoreCase("key", "value2");

        String actualFirst = attributes.getIgnoreCase("KEY");
        String expectedFirst = "value2";
        Assert.assertEquals(expectedFirst, actualFirst);

        String actualSecond = attributes.getIgnoreCase("key");
        String expectedSecond = "value2";
        Assert.assertEquals(expectedSecond, actualSecond);
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        boolean actual = attributes.get("key").equals("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        boolean actualIgnoreCase = attributes.getIgnoreCase("key").equals("");
        boolean expectedIgnoreCase = true;
        Assert.assertEquals(expectedIgnoreCase, actualIgnoreCase);
    }

    @Test
    public void testPutMethodWithAttributeObject() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        attributes.put(attribute);

        String actual = attributes.get(key);
        String expected = value;
        Assert.assertEquals(expected, actual);

        String actualIgnoreCase = attributes.getIgnoreCase(key);
        Assert.assertEquals(expected, actualIgnoreCase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        String actual = attributes.get("key1");
        Assert.assertEquals(actual, "");

        String actualIgnoreCase = attributes.getIgnoreCase("key1");
        Assert.assertEquals(actual, actualIgnoreCase);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("Key1");
        String actual = attributes.get("Key1");
        Assert.assertEquals(actual, "");

        String actualIgnoreCase = attributes.getIgnoreCase("Key1");
        Assert.assertEquals(actual, actualIgnoreCase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove(null);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        boolean actualIgnoreCase = attributes.hasKeyIgnoreCase("key");
        Assert.assertEquals(expected, actualIgnoreCase);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean actual = attributes.hasKeyIgnoreCase("Key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        boolean actualIgnoreCase = attributes.hasKeyIgnoreCase("kEy");
        Assert.assertEquals(expected, actualIgnoreCase);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();

        int actual = attributes.size();
        int expected = 0;
        Assert.assertEquals(expected, actual);

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int actualSize = attributes.size();
        int expectedSize = 2;
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        String actualFirst = attributes3.get("key1");
        String expectedFirst = "value1";
        Assert.assertEquals(expectedFirst, actualFirst);

        String actualSecond = attributes3.get("key2");
        String expectedSecond = "value2";
        Assert.assertEquals(expectedSecond, actualSecond);

        int actualSize = attributes3.size();
        int expectedSize = 2;
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testToStringMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String actual = attributes.toString();
        String expected = "key1=\"value1\" key2=\"value2\"";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value3");
        attributes3.put("key2", "value4");

        boolean actual1 = attributes1.equals(attributes2);
        boolean expected1 = true;
        Assert.assertEquals(expected1, actual1);

        boolean actual2 = attributes1.equals(attributes3);
        boolean expected2 = false;
        Assert.assertEquals(expected2, actual2);
    }
}