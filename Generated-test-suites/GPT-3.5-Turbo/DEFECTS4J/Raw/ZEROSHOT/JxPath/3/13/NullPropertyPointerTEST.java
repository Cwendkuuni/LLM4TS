import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void getNameReturnsCorrectName() {
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void setPropertyIndexDoesNothing() {
        int index = 1;
        nullPropertyPointer.setPropertyIndex(index);
        // No assertion here since the method should have no effect
    }

    @Test
    public void getLengthReturnsZero() {
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void getBaseValueReturnsNull() {
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void getImmediateNodeReturnsNull() {
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void isLeafReturnsTrue() {
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void getValuePointerReturnsInstanceOfNullPointer() {
        NullPointer expectedPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        assertEquals(expectedPointer, nullPropertyPointer.getValuePointer());
    }

    @Test
    public void isActualPropertyReturnsFalse() {
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void isActualReturnsFalse() {
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void isContainerReturnsTrue() {
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void setValueThrowsException() {
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void createPathReturnsParentCreatePath() {
        JXPathContext context = null;
        NullPropertyPointer parent = nullPropertyPointer;
        assertNotNull(parent.createPath(context));
    }

    @Test
    public void createPathWithValueReturnsParentCreatePathWithAttributeValue() {
        JXPathContext context = null;
        NullPropertyPointer parent = nullPropertyPointer;
        Object value = null;
        assertNotNull(parent.createPath(context, value));
    }

    @Test
    public void createChildReturnsCreatePathCreateChild() {
        JXPathContext context = null;
        QName name = new QName("name");
        int index = 1;
        assertNotNull(nullPropertyPointer.createChild(context, name, index));
    }

    @Test
    public void createChildWithValueReturnsCreatePathCreateChildWithValue() {
        JXPathContext context = null;
        QName name = new QName("name");
        int index = 1;
        Object value = null;
        assertNotNull(nullPropertyPointer.createChild(context, name, index, value));
    }

    @Test
    public void getPropertyNameReturnsPropertyName() {
        String expectedPropertyName = "*";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void setPropertyNameSetsPropertyName() {
        String propertyName = "name";
        nullPropertyPointer.setPropertyName(propertyName);
        String expectedPropertyName = propertyName;
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void setNameAttributeValueSetsPropertyName() {
        String attributeValue = "name";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        String expectedPropertyName = attributeValue;
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void isCollectionReturnsFalse() {
        boolean expectedValue = false;
        boolean actualValue = nullPropertyPointer.isCollection();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void getPropertyCountReturnsZero() {
        int expectedCount = 0;
        int actualCount = nullPropertyPointer.getPropertyCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void getPropertyNamesReturnsEmptyArray() {
        String[] expectedNames = new String[0];
        String[] actualNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void asPathReturnsSuperAsPath() {
        String expectedValue = super.asPath();
        String actualValue = nullPropertyPointer.asPath();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void escapeReplacesSingleQuotesWithApostrophes() {
        String string = "test's";
        String expectedString = "test&apos;s";
        String actualString = nullPropertyPointer.escape(string);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void escapeReplacesDoubleQuotesWithQuot() {
        String string = "test\"s";
        String expectedString = "test&quot;s";
        String actualString = nullPropertyPointer.escape(string);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void escapeDoesNotReplaceAnyOtherCharacter() {
        String string = "test";
        String expectedString = "test";
        String actualString = nullPropertyPointer.escape(string);
        assertEquals(expectedString, actualString);
    }
}