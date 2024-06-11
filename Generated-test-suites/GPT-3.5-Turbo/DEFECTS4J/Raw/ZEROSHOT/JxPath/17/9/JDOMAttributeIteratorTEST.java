import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class JDOMAttributeIteratorTest {
  
  private JDOMAttributeIterator attributeIterator;

  @Before
  public void setUp() {
    NodePointer parent = new JDOMNodePointer(new Element("test"), null);
    QName name = new QName("attributeName", "prefix");
    attributeIterator = new JDOMAttributeIterator(parent, name);
  }

  @Test
  public void testGetNodePointer() {
    NodePointer nodePointer = attributeIterator.getNodePointer();
    Assert.assertNotNull(nodePointer);
    Assert.assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
  }

  @Test
  public void testGetPosition() {
    int position = attributeIterator.getPosition();
    Assert.assertEquals(attributeIterator.position, position);
  }

  @Test
  public void testSetPosition() {
    int position = 2;
    boolean success = attributeIterator.setPosition(position);
    Assert.assertTrue(success);
    Assert.assertEquals(position, attributeIterator.getPosition());
  }
}