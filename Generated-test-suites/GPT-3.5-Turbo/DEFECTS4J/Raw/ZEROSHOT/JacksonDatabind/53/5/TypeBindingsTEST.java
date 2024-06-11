import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypeBindingsTest {
    private TypeBindings typeBindings;
  
    @BeforeEach
    void setUp() {
        String[] names = new String[]{"name1", "name2"};
        JavaType[] types = new JavaType[]{new JavaType(), new JavaType()};
        String[] unboundVariables = new String[]{"name1", "name2"};
        
        typeBindings = new TypeBindings(names, types, unboundVariables);
    }
    
    @Test
    void testFindBoundType() {
        JavaType javaType = typeBindings.findBoundType("name1");
        assertEquals(new JavaType(), javaType);
    }
    
    @Test
    void testIsEmpty() {
        assertFalse(typeBindings.isEmpty());
    }
    
    @Test
    void testSize() {
        assertEquals(2, typeBindings.size());
    }
    
    @Test
    void testGetBoundName() {
        String boundName = typeBindings.getBoundName(0);
        assertEquals("name1", boundName);
    }
    
    @Test
    void testGetBoundType() {
        JavaType boundType = typeBindings.getBoundType(0);
        assertEquals(new JavaType(), boundType);
    }
    
    @Test
    void testGetTypeParameters() {
        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assertEquals(Arrays.asList(new JavaType(), new JavaType()), typeParameters);
    }
    
    @Test
    void testWithUnboundVariable() {
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("name3");
        assertEquals(3, newTypeBindings.size());
        assertEquals("name1", newTypeBindings.getBoundName(0));
        assertEquals("name2", newTypeBindings.getBoundName(1));
        assertEquals("name3", newTypeBindings.getBoundName(2));
    }
    
    @Test
    void testHasUnbound() {
        assertTrue(typeBindings.hasUnbound("name1"));
        assertFalse(typeBindings.hasUnbound("name3"));
    }
    
    @Test
    void testHashCode() {
        int hashCode = typeBindings.hashCode();
        assertEquals(1, hashCode);
    }
    
     @Test
    void testEquals() {
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("name3");
        assertFalse(typeBindings.equals(null));
        assertFalse(typeBindings.equals(new Object()));
        assertTrue(typeBindings.equals(typeBindings));
        assertTrue(typeBindings.equals(newTypeBindings));
    }
    
    @Test
    void testToString() {
        String toString = typeBindings.toString();
        assertEquals("<name1,name2>", toString);
    }
}