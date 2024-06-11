
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Attributes;
import org.jsoup.helper.Validate;

public class AttributeTest {
    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get(attribute.getKey()));
    }

    // ... test cases for edge cases and exception handling
}
