/* 
 * This is a Junit 4 test file for the SimpleType class.
 */

package com.fasterxml.jackson.databind.type;
import org.junit.Test;
import java.util.*;
import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void constructUnsafeTest(){
        // Test case for constructUnsafe() method
        SimpleType st = SimpleType.constructUnsafe(Map.class);
        assertNull(st.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructUnsafeTestEx(){
        // Test case for constructUnsafe() method with an invalid argument
        SimpleType.constructUnsafe(HashMap.class);
    }
    
    @Test
    public void constructTest(){
        // Test case for construct() method
        SimpleType st = SimpleType.construct(SimpleType.class);
        assertNull(st.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructTestExMap(){
        // Test case for construct() method with Map class argument
        SimpleType.construct(HashMap.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructTestExCollection(){
        // Test case for construct() method with Collection class argument
        SimpleType.construct(ArrayList.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructTestExArray(){
        // Test case for construct() method with array class argument
        SimpleType.construct(String[].class);
    }
    
    @Test
    public void narrowTest(){
        // Test case for _narrow() method
        SimpleType st1 = new SimpleType(HashMap.class);
        SimpleType st2 = st1._narrow(HashMap.class);
        assertEquals(st1, st2);
    }
    
    @Test
    public void withContentTypeTest(){
        // Test case for withContentType() method
        SimpleType st1 = new SimpleType(ArrayList.class);
        JavaType jt1 = new SimpleType(String.class);
        SimpleType st2 = (SimpleType)st1.withContentType(jt1);
        assertNull(st2.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void withContentTypeTestEx(){
        // Test case for withContentType() method with an invalid operation
        SimpleType st1 = new SimpleType(ArrayList.class);
        JavaType jt1 = new SimpleType(String.class);
        SimpleType st2 = (SimpleType)st1.withContentType(jt1);
        st2.withContentType(jt1);
    }
    
    @Test
    public void withTypeHandlerTest(){
        // Test case for withTypeHandler() method
        SimpleType st1 = new SimpleType(ArrayList.class);
        Object handler = new Object();
        SimpleType st2 = st1.withTypeHandler(handler);
        assertEquals(handler, st2.getTypeHandler());
    }
    
    @Test
    public void withContentTypeHandlerTest(){
        // Test case for withContentTypeHandler() method
        SimpleType st1 = new SimpleType(ArrayList.class);
        Object handler = new Object();
        SimpleType st2 = (SimpleType)st1.withTypeHandler(handler);
        JavaType jt1 = new SimpleType(String.class);
        SimpleType st3 = null;
        try{
            st3 = (SimpleType)st2.withContentTypeHandler(jt1);
        }catch(IllegalArgumentException e){
        }
        assertNull(st3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void withContentValueHandlerTestEx(){
        // Test case for withContentValueHandler() method with an invalid operation
        SimpleType st1 = new SimpleType(ArrayList.class);
        Object handler = new Object();
        st1.withContentValueHandler(handler);
    }
    
    @Test
    public void withStaticTypingTest(){
        // Test case for withStaticTyping() method
        SimpleType st1 = new SimpleType(ArrayList.class);
        SimpleType st2 = st1.withStaticTyping();
        assertTrue(st2.isStatic());
    }
    
    @Test
    public void refineTest(){
        // Test case for refine() method
        SimpleType st1 = new SimpleType(ArrayList.class);
        assertNull(st1.refine(ArrayList.class,null,null,null));
    }
    
    @Test
    public void buildCanonicalNameTest(){
        // Test case for buildCanonicalName() method
        SimpleType st1 = new SimpleType(String.class);
        assertEquals("java.util.ArrayList<java.lang.String>", st1.buildCanonicalName());
    }
    
    @Test
    public void isContainerTypeTest(){
        // Test case for isContainerType() method
        SimpleType st = new SimpleType(Stack.class);
        assertFalse(st.isContainerType());
    }
    
    @Test
    public void getErasedSignatureTest(){
        // Test case for getErasedSignature() method
        SimpleType st = new SimpleType(Stack.class);
        StringBuilder sb = st.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/util/Stack<*>;", sb.toString());
    }
    
    @Test
    public void getGenericSignatureTest(){
        // Test case for getGenericSignature() method
        SimpleType st = new SimpleType(Stack.class);
        StringBuilder sb = st.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/util/Stack<*>;", sb.toString());
    }
    
    @Test
    public void toStringTest(){
        // Test case for toString() method
        SimpleType st = new SimpleType(Stack.class);
        assertEquals("[simple type, class Ljava.util.Stack]", st.toString());
    }
    
    @Test
    public void equalsTest(){
        // Test case for equals() method
        SimpleType st1 = new SimpleType(Stack.class);
        SimpleType st2 = new SimpleType(Stack.class);
        assertEquals(st1,st2);
    }

}