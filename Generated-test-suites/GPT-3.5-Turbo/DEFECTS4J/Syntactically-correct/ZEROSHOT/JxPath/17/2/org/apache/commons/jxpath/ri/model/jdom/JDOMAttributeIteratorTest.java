import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class JDOMAttributeIteratorTest {
  
  private JDOMAttributeIterator jdomAttributeIterator;
  private NodePointer parent;
  
  @Before
  public void setUp() {
    parent = createMockNodePointer();
    jdomAttributeIterator = new JDOMAttributeIterator(parent, new QName("attributeName"));
  }
  
  @Test
  public void testGetNodePointer() {
    NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
    assertEquals(parent, nodePointer.getParent());
    assertTrue(nodePointer.getNode() instanceof Attribute);
  }

  @Test
  public void testGetPosition() {
    assertEquals(1, jdomAttributeIterator.getPosition());
  }

  @Test
  public void testSetPositionWhenValidPosition() {
    assertTrue(jdomAttributeIterator.setPosition(1));
    assertEquals(1, jdomAttributeIterator.getPosition());
  }

  @Test
  public void testSetPositionWhenInvalidPosition() {
    assertFalse(jdomAttributeIterator.setPosition(0));
    assertEquals(1, jdomAttributeIterator.getPosition());
  }
  
  private NodePointer createMockNodePointer() {
    return new NodePointer() {
      @Override
      public Object getNode() {
        return new Element("test");
      }

      @Override
      public NodePointer getParent() {
        return null;
      }

      @Override
      public String asPath() {
        return null;
      }

      @Override
      public Object getBaseValue() {
        return null;
      }

      @Override
      public QName getName() {
        return null;
      }

      @Override
      public Object getImmediateNode() {
        return null;
      }
    };
  }
}