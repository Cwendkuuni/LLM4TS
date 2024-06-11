package org.mockito.internal.stubbing.defaultanswers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswerForToStringMethod() {
        String result = (String) returnsEmptyValues.answer(invocation("toString"));
        assertTrue(result.startsWith("Mock for "));
    }
    
    @Test
    public void testAnswerForCompareToMethod() {
        assertEquals(0, returnsEmptyValues.answer(invocation("compareTo", new Object[] { new Object() })));
        assertEquals(1, returnsEmptyValues.answer(invocation("compareTo", new Object[] { new ReturnsEmptyValues() })));
    }
    
    @Test
    public void testAnswerForPrimitiveOrWrapperType() {
        assertEquals(0, returnsEmptyValues.answer(invocation("intMethod")));
        assertEquals(0, returnsEmptyValues.answer(invocation("IntegerMethod")));
        assertEquals(false, returnsEmptyValues.answer(invocation("booleanMethod")));
        assertEquals(false, returnsEmptyValues.answer(invocation("BooleanMethod")));
        assertEquals('\u0000', returnsEmptyValues.answer(invocation("charMethod")));
        assertEquals('\u0000', returnsEmptyValues.answer(invocation("CharacterMethod")));
        assertEquals(0d, returnsEmptyValues.answer(invocation("doubleMethod")));
        assertEquals(0d, returnsEmptyValues.answer(invocation("DoubleMethod")));
        assertEquals(0f, returnsEmptyValues.answer(invocation("floatMethod")));
        assertEquals(0f, returnsEmptyValues.answer(invocation("FloatMethod")));
        assertEquals(0L, returnsEmptyValues.answer(invocation("longMethod")));
        assertEquals(0L, returnsEmptyValues.answer(invocation("LongMethod")));
        assertEquals((short) 0, returnsEmptyValues.answer(invocation("shortMethod")));
        assertEquals((short) 0, returnsEmptyValues.answer(invocation("ShortMethod")));
        assertEquals((byte) 0, returnsEmptyValues.answer(invocation("byteMethod")));
        assertEquals((byte) 0, returnsEmptyValues.answer(invocation("ByteMethod")));
    }
    
    @Test
    public void testAnswerForCollectionType() {
        Collection<Object> expectedList = new LinkedList<Object>();
        assertEquals(expectedList, returnsEmptyValues.answer(invocation("collectionMethod")));
        assertEquals(expectedList, returnsEmptyValues.answer(invocation("listMethod")));
        
        Set<Object> expectedSet = new HashSet<Object>();
        assertEquals(expectedSet, returnsEmptyValues.answer(invocation("setMethod")));
        assertEquals(expectedSet, returnsEmptyValues.answer(invocation("hashSetMethod")));
        
        SortedSet<Object> expectedSortedSet = new TreeSet<Object>();
        assertEquals(expectedSortedSet, returnsEmptyValues.answer(invocation("sortedSetMethod")));
        assertEquals(expectedSortedSet, returnsEmptyValues.answer(invocation("treeSetMethod")));
        
        LinkedHashSet<Object> expectedLinkedHashSet = new LinkedHashSet<Object>();
        assertEquals(expectedLinkedHashSet, returnsEmptyValues.answer(invocation("linkedHashSetMethod")));
        
        List<Object> expectedLinkedList = new LinkedList<Object>();
        assertEquals(expectedLinkedList, returnsEmptyValues.answer(invocation("linkedListMethod")));
        assertEquals(expectedLinkedList, returnsEmptyValues.answer(invocation("arrayListMethod")));
    }
    
    @Test
    public void testAnswerForMapType() {
        Map<Object, Object> expectedMap = new HashMap<Object, Object>();
        assertEquals(expectedMap, returnsEmptyValues.answer(invocation("mapMethod")));
        assertEquals(expectedMap, returnsEmptyValues.answer(invocation("hashMapMethod")));
        
        SortedMap<Object, Object> expectedSortedMap = new TreeMap<Object, Object>();
        assertEquals(expectedSortedMap, returnsEmptyValues.answer(invocation("sortedMapMethod")));
        assertEquals(expectedSortedMap, returnsEmptyValues.answer(invocation("treeMapMethod")));
        
        LinkedHashMap<Object, Object> expectedLinkedHashMap = new LinkedHashMap<Object, Object>();
        assertEquals(expectedLinkedHashMap, returnsEmptyValues.answer(invocation("linkedHashMapMethod")));
    }
    
    private InvocationOnMock invocation(String methodName, Object[] arguments) {
        return new InvocationOnMock() {
            public Object getMock() {return null;}
            public Object[] getArguments() {return arguments;}
            public void setAnswer(Object... answer) {}
            public ObjectAnswer getAnswer() {return null;}
            public ObjectAnswer getDefaultAnswer() {return null;}
            public Class<?> getRawReturnType() {return null;}
            public Class<?> getReturnType() {return null;}
            public MockitoFramework getMockitoFramework() {return null;}
            public InvocationContainer getInvocationContainer() {return null;}
            public void markVerified() {}
            public boolean isVerified() {return false;}
            public VerificationMode getDescription() {return null;}
            public Location getLocation() {return null;}
            public boolean isIgnoredForVerification() {return false;}
            public void ignoreForVerification() {}
            public void markStubbed(Answer<?> answer) {}
            public boolean isDontVerify() {return false;}
            public void dontVerify() {}
            public void verified(VerificationMode mode) {}
            public RealMethodBehavior getRealMethodBehavior() {return null;}
            public Answer<Object> getDefaultAnswer() {return null;}
            public boolean isVoid() {return false;}
            public String printDiff() {return null;}
            public List<Object> getAnswers() {return null;}
            public void addAnswer(Object answer) {}
            public GenericSignature getGenericSignature() {return null;}
            public MockitoMethod getMockitoMethod() {return null;}
            public boolean isVarArgs() {return false;}
            public ObjectAnswer getReturns() {return null;}
            public InvocationMatcher getInvocationMatcher() {return null;}
            public int getSequenceNumber() {return 0;}
            public Invocation getInstance() {return null;}
            public void markStubbed() {}
            public void skipReturn() {}
            public void markStubbedVoid() {}
            public boolean isVerified(VerificationMode mode) {return false;}
            public Object[] getArgumentsAsArray() {return null;}
            public Throwable getCauseOfFailure() {return null;}
            public void setMethodForStubbing(MockitoMethod method) {}
            public ObjectAnswer getReturnsEmptyValues() {return null;}
        };
    }
    
    private InvocationOnMock invocation(String methodName) {
        return invocation(methodName, new Object[0]);
    }
}