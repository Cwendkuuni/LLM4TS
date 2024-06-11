import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jmock.Mockery;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class JDOMAttributeIteratorTest {
    private Mockery context;
    private JDOMAttributeIterator iterator;
    private NodePointer mockParent;
    private Element mockElement;
    private Attribute testAttribute1;
    private Attribute testAttribute2;
    private Attribute testAttribute3;
    
    @Before
    public void setUp() {
        context = new Mockery();
        mockParent = context.mock(NodePointer.class);
        mockElement = context.mock(Element.class);
        testAttribute1 = new Attribute("testAttribute1", "value1");
        testAttribute2 = new Attribute("testAttribute2", "value2");
        testAttribute3 = new Attribute("testAttribute3", "value3");
        iterator = new JDOMAttributeIterator(mockParent, new QName(""));
    }
    
    @Test
    public void testConstructorNoAttributes() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getAttributes(); will(returnValue(Collections.emptyList()));
        }});
        
        assertThat(iterator.getNodePointer(), is(nullValue()));
    }
    
    @Test
    public void testConstructorWithPrefix() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getNamespace(with(equal("prefix"))); will(returnValue(Namespace.getNamespace("prefix")));
            allowing(mockElement).getAttribute(with(any(String.class)), with(any(Namespace.class))); will(returnValue(null));
        }});
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("prefix:name"));
        assertThat(iterator.getNodePointer(), is(nullValue()));
    }
    
    @Test
    public void testConstructorWithValidAttribute() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getNamespace(with(any(String.class))); will(returnValue(Namespace.getNamespace("")));
            allowing(mockElement).getAttribute(with(equal("testAttribute")), with(any(Namespace.class))); will(returnValue(testAttribute1));
        }});
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("testAttribute"));
        assertThat(iterator.getNodePointer(), is(notNullValue()));
        assertThat(iterator.getPosition(), is(equalTo(1)));
    }
    
    @Test
    public void testConstructorWithWildcardAttribute() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getNamespace(with(any(String.class))); will(returnValue(Namespace.NO_NAMESPACE));
            allowing(mockElement).getAttributes(); will(returnValue(Arrays.asList(testAttribute1, testAttribute2, testAttribute3)));
        }});
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("*"));
        assertThat(iterator.getNodePointer(), is(notNullValue()));
        assertThat(iterator.getPosition(), is(equalTo(1)));
    }
    
    @Test
    public void testGetPosition() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getNamespace(with(any(String.class))); will(returnValue(Namespace.NO_NAMESPACE));
            allowing(mockElement).getAttributes(); will(returnValue(Arrays.asList(testAttribute1, testAttribute2, testAttribute3)));
        }});
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("*"));
        iterator.setPosition(2);
        assertThat(iterator.getPosition(), is(equalTo(2)));
    }
    
    @Test
    public void testSetPositionValid() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getNamespace(with(any(String.class))); will(returnValue(Namespace.NO_NAMESPACE));
            allowing(mockElement).getAttributes(); will(returnValue(Arrays.asList(testAttribute1, testAttribute2, testAttribute3)));
        }});
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("*"));
        boolean result = iterator.setPosition(3);
        assertThat(result, is(true));
        assertThat(iterator.getPosition(), is(equalTo(3)));
    }
    
    @Test
    public void testSetPositionInvalid() {
        context.checking(new Expectations() {{
            allowing(mockParent).getNode(); will(returnValue(mockElement));
            allowing(mockElement).getNamespace(with(any(String.class))); will(returnValue(Namespace.NO_NAMESPACE));
            allowing(mockElement).getAttributes(); will(returnValue(Arrays.asList(testAttribute1, testAttribute2, testAttribute3)));
        }});
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("*"));
        boolean result = iterator.setPosition(4);
        assertThat(result, is(false));
        assertThat(iterator.getPosition(), is(equalTo(0)));
    }
}