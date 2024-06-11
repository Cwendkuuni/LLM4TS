import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertFalse(nullPropertyPointer.isLeaf());
        assertEquals(0, nullPropertyPointer.getLength());
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testGetNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName("testProperty");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
        assertEquals(expectedName.getNamespaceURI(), actualName.getNamespaceURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexMethodWithInvalidIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(Integer.MIN_VALUE);
    }

    @Test
    public void testGetLengthMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValueMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNodeMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeafMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer expectedNullPointer = new NullPointer(null, new QName(""));
        NodePointer actualNullPointer = nullPropertyPointer.getValuePointer();
        assertEquals(expectedNullPointer.getClass(), actualNullPointer.getClass());
        assertEquals(expectedNullPointer.getParentPointer(), actualNullPointer.getParentPointer());
        assertEquals(expectedNullPointer.getName(), actualNullPointer.getName());
    }

    @Test
    public void testIsActualPropertyMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodWithNonPropertyOwnerParent() {
        NodePointer nonPropertyOwnerParent = new DOMNodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerParent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePathMethod() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer expectedNullParent = new NullPointer(null, new QName(""));
        NodePointer actualNullParent = nullPropertyPointer.createPath(context);
        assertEquals(expectedNullParent.getClass(), actualNullParent.getClass());
        assertEquals(expectedNullParent.getParentPointer(), actualNullParent.getParentPointer());
        assertEquals(expectedNullParent.getName(), actualNullParent.getName());
    }

    @Test
    public void testCreatePathMethodWithValue() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object testValue = new Object();
        NodePointer expectedValueParent = new NullPointer(null, new QName(""));
        NodePointer actualValueParent = nullPropertyPointer.createPath(context, testValue);
        assertEquals(expectedValueParent.getClass(), actualValueParent.getClass());
        assertEquals(expectedValueParent.getParentPointer(), actualValueParent.getParentPointer());
        assertEquals(expectedValueParent.getName(), actualValueParent.getName());
        assertEquals(testValue, actualValueParent.getValue());
    }

    @Test
    public void testCreateChildMethodWithNullParent() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName testName = new QName("testName");
        NodePointer expectedChild = nullPropertyPointer.createPath(context).createChild(context, testName);
        NodePointer actualChild = nullPropertyPointer.createChild(context, testName, 0);
        assertEquals(expectedChild.getClass(), actualChild.getClass());
        assertEquals(expectedChild.getParentPointer(), actualChild.getParentPointer());
        assertEquals(expectedChild.getName(), actualChild.getName());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreateChildMethodWithInvalidParent() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName testName = new QName("testName");
        NodePointer invalidParent = new DOMNodePointer(null, null);
        nullPropertyPointer.createChild(context, testName, 0, null);
    }

    @Test
    public void testCreateChildMethodWithNullName() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer expectedChild = nullPropertyPointer.createPath(context).createChild(context, null, 0);
        NodePointer actualChild = nullPropertyPointer.createChild(context, null, 0);
        assertNull(actualChild);
        assertEquals(expectedChild, null);
    }

    @Test
    public void testCreateChildMethodWithEmptyName() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName testName = new QName("");
        NodePointer expectedChild = nullPropertyPointer.createPath(context).createChild(context, testName, 0);
        NodePointer actualChild = nullPropertyPointer.createChild(context, testName, 0);
        assertNull(actualChild);
        assertEquals(expectedChild, null);
    }

    @Test
    public void testCreateChildMethodWithName() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName testName = new QName("testName");
        NodePointer expectedChild = nullPropertyPointer.createPath(context).createChild(context, testName, 0);
        NodePointer actualChild = nullPropertyPointer.createChild(context, testName, 0);
        assertEquals(expectedChild.getClass(), actualChild.getClass());
        assertEquals(expectedChild.getParentPointer(), actualChild.getParentPointer());
        assertEquals(expectedChild.getName(), actualChild.getName());
    }

    @Test
    public void testCreateChildMethodWithIndexOutOfBounds() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName testName = new QName("testName");
        nullPropertyPointer.setPropertyName("testProperty");
        NodePointer expectedChild = nullPropertyPointer.createPath(context).createChild(context, testName, Integer.MAX_VALUE);
        NodePointer actualChild = nullPropertyPointer.createChild(context, testName, Integer.MAX_VALUE);
        assertNull(actualChild);
        assertEquals(expectedChild, null);
    }

    @Test
    public void testSetPropertyNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        String expectedPropertyName = "testProperty";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testSetNameAttributeValueMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("testProperty");
        String expectedPropertyName = "testProperty";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollectionMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testgetPropertyCountMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testgetPropertyNamesMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] expectedNames = new String[0];
        assertArrayEquals(expectedNames, nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPathMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String expectedPath = "";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);

        NullPropertyPointer propertyByNameNullParent = new NullPropertyPointer(null);
        propertyByNameNullParent.setPropertyName("testProperty");
        propertyByNameNullParent.setNameAttributeValue("testName");
        String expectedPathByName = "[@name='testName']";
        String actualPathByName = propertyByNameNullParent.asPath();
        assertEquals(expectedPathByName, actualPathByName);

        NullPropertyPointer propertyByNameParent = new NullPropertyPointer(new NullPointer(null, new QName("")));
        propertyByNameParent.setPropertyName("testProperty");
        propertyByNameParent.setNameAttributeValue("testName");
        String expectedPathByParentName = "[@name='testName']";
        NodePointer parent = new NullPointer(null, new QName(""));
        NodePointer child = propertyByNameParent.createPath(new JXPathContext(null)).createChild(new JXPathContext(null), new QName("testChild"));
        String expectedPathByParent = child.asPath();
        String actualPathByParent = propertyByNameParent.asPath();
        assertEquals(expectedPathByParentName, actualPathByParent.substring(0, actualPathByParent.lastIndexOf("/")) + "/" + expectedPathByParentName);
        assertEquals(expectedPathByParent, expectedPathByParent.substring(0, expectedPathByParent.lastIndexOf("/")) + "/" + expectedPathByParentName);
    }

    @Test
    public void testEscapeMethod() {
        String testString = "testString with 'single quotes' and \"double quotes\"";
        String expectedEscapedString = "testString with &apos;single quotes&apos; and &quot;double quotes&quot;";
        String actualEscapedString = NullPropertyPointer.escape(testString);
        assertEquals(expectedEscapedString, actualEscapedString);
    }
}