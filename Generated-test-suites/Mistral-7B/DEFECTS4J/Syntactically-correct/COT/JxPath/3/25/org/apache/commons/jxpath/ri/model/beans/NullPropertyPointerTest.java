import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected parent to be set", parent, nullPropertyPointer.getParent());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        Assert.assertEquals("Expected name to be *", expectedName, actualName);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();
        Assert.assertEquals("Expected length to be 0", expectedLength, actualLength);
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        Object expectedBaseValue = null;
        Object actualBaseValue = nullPropertyPointer.getBaseValue();
        Assert.assertEquals("Expected base value to be null", expectedBaseValue, actualBaseValue);
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        NodePointer expectedImmediateNode = new NodePointer(new Object());
        NodePointer actualImmediateNode = nullPropertyPointer.getImmediateNode();
        Assert.assertEquals("Expected immediate node to be a new NodePointer with an object", expectedImmediateNode, actualImmediateNode);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean expectedLeaf = true;
        boolean actualLeaf = nullPropertyPointer.isLeaf();
        Assert.assertEquals("Expected leaf to be true", expectedLeaf, actualLeaf);
    }

    @Test
    public void testgetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName("*"));
        Assert.assertEquals("Expected valuePointer to be a NullPointer", nullPointer, nullPropertyPointer.getValuePointer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean actualIsActualProperty = nullPropertyPointer.isActualProperty();
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean actualIsActual = nullPropertyPointer.isActual();
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean expectedContainer = true;
        boolean actualContainer = nullPropertyPointer.isContainer();
        Assert.assertEquals("Expected container to be true", expectedContainer, actualContainer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setValue("testValue");
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expectedPath = new NodePointer(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context);
        Assert.assertEquals("Expected path to be a new NodePointer with an object", expectedPath, actualPath);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer expectedPath = new NodePointer(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context, value);
        Assert.assertEquals("Expected path to be a new NodePointer with an object", expectedPath, actualPath);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        int index = 1;
        NodePointer expectedChild = new NodePointer(new Object());
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, index);
        Assert.assertEquals("Expected child to be a new NodePointer with an object", expectedChild, actualChild);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        int index = 1;
        Object value = new Object();
        NodePointer expectedChild = new NodePointer(value);
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, index, value);
        Assert.assertEquals("Expected child to have the given value", value, actualChild.getValue());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        String propertyName = "testPropertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        String expectedPropertyName = propertyName;
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        Assert.assertEquals("Expected propertyName to be set to testPropertyName", expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        String attributeValue = "testAttributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        String expectedAttributeValue = attributeValue;
        String actualAttributeValue = nullPropertyPointer.getPropertyName();
        Assert.assertEquals("Expected attributeValue to be set to testAttributeValue", expectedAttributeValue, actualAttributeValue);
        boolean isByNameAttribute = nullPropertyPointer.isByNameAttribute();
        Assert.assertTrue("Expected isByNameAttribute to be true", isByNameAttribute);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testIsCollectionWhenNotCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean actualIsCollection = nullPropertyPointer.isCollection();
        Assert.assertFalse("Expected isCollection to be false", actualIsCollection);
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        int expectedPropertyCount = 0;
        int actualPropertyCount = nullPropertyPointer.getPropertyCount();
        Assert.assertEquals("Expected propertyCount to be 0", expectedPropertyCount, actualPropertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        String[] expectedPropertyNames = new String[0];
        String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals("Expected propertyNames to be an empty array", expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testAsPathWhenByNameAttributeIsFalse() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        String expectedAsPath = "";
        String actualAsPath = nullPropertyPointer.asPath();
        Assert.assertEquals("Expected asPath to be empty string", expectedAsPath, actualAsPath);
    }

    @Test
    public void testAsPathWhenByNameAttributeIsTrue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String propertyName = "testPropertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        nullPropertyPointer.setNameAttributeValue("testAttributeName");
        int index = 1;
        String expectedAsPath = "[@testAttributeName='testPropertyName']";
        String actualAsPath = nullPropertyPointer.asPath();
        Assert.assertEquals("Expected asPath to be testAttributeName='testPropertyName'", expectedAsPath, actualAsPath);
    }

    @Test
    public void testEscape() {
        String stringToEscape = "testStringWithSingleQuotes' and doubleQuotes\"";
        String expectedEscapedString = "testStringWithSingleQuotes&apos; and doubleQuotes&quot;";
        String actualEscapedString = NullPropertyPointer.escape(stringToEscape);
        Assert.assertEquals("Expected escapedString to be testStringWithSingleQuotes&apos; and doubleQuotes&quot;", expectedEscapedString, actualEscapedString);
    }
}