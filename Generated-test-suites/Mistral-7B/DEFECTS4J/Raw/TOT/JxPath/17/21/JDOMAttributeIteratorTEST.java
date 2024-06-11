package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.junit.Assert;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.List;

public class JDOMAttributeIteratorTest {

    private static final String TEST_XML = "<root xmlns:x='http://test.com'>" +
                                            "<child attr1='value1' attr2='value2'/>" +
                                            "</root>";

    @org.junit.Test
    public void testBasic() {
        JXPathContext context = new JXPathContext(buildDocument(TEST_XML));
        NodeIterator iterator = context.iterate("/root/child/@*[local-name()='attr1']");
        NodePointer nodePointer = iterator.next();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("value1", attributePointer.getValue().getValue());
        Assert.assertEquals("attr1", attributePointer.getName().getName());
        Assert.assertEquals("http://test.com", attributePointer.getName().getPrefix());
    }

    @org.junit.Test
    public void testIterateAll() {
        JXPathContext context = new JXPathContext(buildDocument(TEST_XML));
        NodeIterator iterator = context.iterate("/root/child/@*");
        int position = 0;
        while (iterator.hasNext()) {
            NodePointer nodePointer = iterator.next();
            JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
            if (position++ == 0) {
                Assert.assertEquals("attr1", attributePointer.getName().getName());
                Assert.assertEquals("value1", attributePointer.getValue().getValue());
                Assert.assertEquals("http://test.com", attributePointer.getName().getPrefix());
            } else {
                Assert.assertEquals("attr2", attributePointer.getName().getName());
                Assert.assertEquals("value2", attributePointer.getValue().getValue());
                Assert.assertNull(attributePointer.getName().getPrefix());
            }
        }
    }

    @org.junit.Test
    public void testEmpty() {
        JXPathContext context = new JXPathContext(buildDocument("<root/>"));
        NodeIterator iterator = context.iterate("/root/child/@*");
        Assert.assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNullParent() {
        JXPathContext context = new JXPathContext(null);
        NodeIterator iterator = context.iterate("/root/child/@*");
        Assert.assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNonExistingChild() {
        JXPathContext context = new JXPathContext(buildDocument(TEST_XML));
        NodeIterator iterator = context.iterate("/root/nonExistingChild/@*");
        Assert.assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeWithNoNameSpace() {
        JXPathContext context = new JXPathContext(buildDocument("<root attr='value'/>"));
        NodeIterator iterator = context.iterate("/root/@attr");
        NodePointer nodePointer = iterator.next();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("attr", attributePointer.getName().getName());
        Assert.assertNull(attributePointer.getName().getPrefix());
    }

    @org.junit.Test
    public void testAttributeWithNamespace() {
        JXPathContext context = new JXPathContext(buildDocument("<root xmlns:x='http://test.com'><child x:attr='value'/></root>"));
        NodeIterator iterator = context.iterate("/root/child/@x:attr");
        NodePointer nodePointer = iterator.next();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("attr", attributePointer.getName().getName());
        Assert.assertEquals("http://test.com", attributePointer.getName().getPrefix());
    }

    private Element buildDocument(String xml) {
        return new org.jdom2.Document(new StringReader(xml)).getRootElement();
    }
}