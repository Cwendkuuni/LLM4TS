package org.jsoup.nodes; // Make sure to import the class under test

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AttributesTest {

    private static final String TEST_KEY = "testKey";
    private static final String TEST_VALUE = "testValue";
    private static final String TEST_IGNORE_CASE_KEY = "TestKey";
    private static final String EMPTY_STRING = "";
    private static final String DATA_KEY = "data-testKey";
    private Attributes attributes;

    public AttributesTest(boolean isClone) {
        this.attributes = isClone ? new Attributes().clone() : new Attributes();
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {true},
                {false}
        });
    }

    @Before
    public void setUp() {
        attributes.put(TEST_KEY, TEST_VALUE);
    }

    @Test
    public void testGet() {
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals(TEST_VALUE, attributes.getIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testGetEmpty() {
        assertEquals(EMPTY_STRING, attributes.get(EMPTY_STRING));
    }

    @Test
    public void testGetNull() {
        assertNull(attributes.get(null));
    }

    @Test
    public void testPut() {
        attributes.put(TEST_IGNORE_CASE_KEY, TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.getIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testPutReplace() {
        attributes.put(TEST_KEY, TEST_IGNORE_CASE_KEY);
        assertEquals(TEST_IGNORE_CASE_KEY, attributes.get(TEST_KEY));
    }

    @Test
    public void testPutIgnoreCaseReplace() {
        attributes.putIgnoreCase(TEST_IGNORE_CASE_KEY, TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.getIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testPutBooleanValue() {
        attributes.put(TEST_KEY, false);
        assertFalse(attributes.hasKey(TEST_KEY));
        assertFalse(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testPutBooleanValueReplace() {
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.put(TEST_KEY, false);
        assertFalse(attributes.hasKey(TEST_KEY));
        assertFalse(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testPutIgnoreCaseBooleanValueReplace() {
        attributes.putIgnoreCase(TEST_IGNORE_CASE_KEY, TEST_VALUE);
        attributes.putIgnoreCase(TEST_IGNORE_CASE_KEY, false);
        assertFalse(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey(TEST_KEY));
        assertTrue(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testHasKeyFalse() {
        assertFalse(attributes.hasKey(EMPTY_STRING));
        assertFalse(attributes.hasKeyIgnoreCase(EMPTY_STRING));
    }

    @Test
    public void testHasKeyNull() {
        assertFalse(attributes.hasKey(null));
    }

    @Test
    public void testSize() {
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemove() {
        attributes.remove(TEST_KEY);
        assertFalse(attributes.hasKey(TEST_KEY));
        assertFalse(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase(TEST_IGNORE_CASE_KEY);
        assertFalse(attributes.hasKey(TEST_KEY));
        assertFalse(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testRemoveEmptyKey() {
        attributes.remove(EMPTY_STRING);
        assertFalse(attributes.hasKey(EMPTY_STRING));
        assertFalse(attributes.hasKeyIgnoreCase(EMPTY_STRING));
    }

    @Test
    public void testRemoveNullKey() {
        try {
            attributes.remove(null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddAll() {
        Attributes attributesToAdd = new Attributes();
        attributesToAdd.put(TEST_KEY, TEST_VALUE);
        attributesToAdd.put(TEST_IGNORE_CASE_KEY, TEST_VALUE);
        attributes.addAll(attributesToAdd);
        assertEquals(3, attributes.size());
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
        assertEquals(TEST_VALUE, attributes.getIgnoreCase(TEST_IGNORE_CASE_KEY));
    }

    @Test
    public void testAddAllEmpty() {
        Attributes attributesToAdd = new Attributes();
        attributes.addAll(attributesToAdd);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testIterator() {
        Iterator<Attribute> iterator = attributes.iterator();
        Attribute attribute = iterator.next();
        assertSame(TEST_KEY, attribute.getKey());
        assertSame(TEST_VALUE, attribute.getValue());
        assertNull(iterator.next());
    }

    @Test
    public void testAsList() {
        List<Attribute> list = attributes.asList();
        Attribute attribute = list.get(0);
        assertSame(TEST_KEY, attribute.getKey());
        assertSame(TEST_VALUE, attribute.getValue());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataSet = attributes.dataset();
        String dataKey = DATA_KEY;
        String dataValue = TEST_VALUE;
        assertEquals(1, dataSet.size());
        assertEquals(dataValue, dataSet.get(dataKey));
    }

    @Test
    public void testEquals() {
        Attributes equalAttributes = new Attributes().put(TEST_KEY, TEST_VALUE);
        Attributes notEqualAttributes = new Attributes();
        Attributes sameClone = new Attributes().clone();
        Attributes differentCapacityClone = new Attributes(new Attributes().clone()).put(TEST_KEY, TEST_VALUE);
        Attributes differentValuesClone = new Attributes().clone().put(TEST_KEY, "otherValue");
        assertEquals(attributes, attributes);
        assertEquals(attributes, sameClone);
        assertEquals(attributes, equalAttributes);
        assertNotEquals(attributes, notEqualAttributes);
        assertNotEquals(attributes, differentCapacityClone);
        assertNotEquals(attributes, differentValuesClone);
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        for (int i = 0; i < attributes.size(); i++) {
            assertEquals(attributes.get(i), clonedAttributes.get(i));
        }
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneNotSupportedException() {
        throw new CloneNotSupportedException();
    }

    @Test
    public void testNormalize() {
        attributes.putIgnoreCase(TEST_IGNORE_CASE_KEY, TEST_VALUE);
        attributes.normalize();
        assertEquals(TEST_KEY, attributes.get(TEST_KEY));
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
        assertFalse(attributes.hasKeyIgnoreCase(TEST_IGNORE_CASE_KEY));
    }
}