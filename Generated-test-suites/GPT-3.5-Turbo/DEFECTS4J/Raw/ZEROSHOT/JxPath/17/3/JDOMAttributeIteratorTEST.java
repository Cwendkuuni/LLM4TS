import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.util.ValueUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
  
  private Document document;
  
  @Before
  public void setUp() {
    Element root = new Element("root");
    root.setAttribute("attr1", "value1");
    root.setAttribute("attr2", "value2");
    root.setAttribute("attr3", "value3");
    root.setAttribute("attr4", "value4");
    
    Element child1 = new Element("child1");
    child1.setAttribute("attr1", "child1_value1");
    child1.setAttribute("attr2", "child1_value2");
    child1.setAttribute("attr3", "child1_value3");
    
    Element child2 = new Element("child2");
    child2.setAttribute("attr1", "child2_value1");
    
    root.addContent(child1);
    root.addContent(child2);
    
    document = new Document(root);
  }
  
  @Test
  public void testJDOMAttributeIterator() {
    JXPathContext context = JXPathContext.newContext(document);
    
    JDOMAttributeIterator iterator = 
        new JDOMAttributeIterator(context.getPointer("/root"), null);
  
    assertEquals(4, getAttributeCount(iterator));
    
    iterator = new JDOMAttributeIterator(context.getPointer("/root"), new QName("attr1"));
    assertEquals(1, getAttributeCount(iterator));
    
    iterator = new JDOMAttributeIterator(context.getPointer("/root"), new QName("attr2"));
    assertEquals(1, getAttributeCount(iterator));
    
    iterator = new JDOMAttributeIterator(context.getPointer("/root/child1"), null);
    assertEquals(3, getAttributeCount(iterator));
    
    iterator = new JDOMAttributeIterator(context.getPointer("/root/child1"), new QName("attr1"));
    assertEquals(1, getAttributeCount(iterator));
    
    iterator = new JDOMAttributeIterator(context.getPointer("/root/child2"), null);
    assertEquals(1, getAttributeCount(iterator));
    
    iterator = new JDOMAttributeIterator(context.getPointer("/root/child2"), new QName("attr1"));
    assertEquals(1, getAttributeCount(iterator));
  }
  
  @Test
  public void testGetPosition() {
    JXPathContext context = JXPathContext.newContext(document);
    
    JDOMAttributeIterator iterator = 
        new JDOMAttributeIterator(context.getPointer("/root"), null);
  
    assertEquals(0, iterator.getPosition());
    
    iterator.setPosition(1);
    assertEquals(1, iterator.getPosition());
    
    iterator.setPosition(5);
    assertEquals(5, iterator.getPosition());
    
    iterator.setPosition(0);
    assertEquals(0, iterator.getPosition());
    
    iterator.setPosition(6);
    assertEquals(6, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    JXPathContext context = JXPathContext.newContext(document);
    
    JDOMAttributeIterator iterator = 
        new JDOMAttributeIterator(context.getPointer("/root"), null);
  
    assertTrue(iterator.setPosition(1));
    assertTrue(iterator.setPosition(2));
    assertTrue(iterator.setPosition(3));
    assertTrue(iterator.setPosition(4));
    assertFalse(iterator.setPosition(0));
    assertFalse(iterator.setPosition(5));
  }
  
  private int getAttributeCount(JDOMAttributeIterator iterator) {
    int count = 0;
    while (iterator.setPosition(++count)) {}
    return count - 1;
  }
}