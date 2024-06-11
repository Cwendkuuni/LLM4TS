import org.junit.*;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        JSType other = new JSType(registry);
        
        boolean result = arrowType.isSubtype(other);
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        boolean tolerateUnknowns = false;
        
        boolean result = arrowType1.hasEqualParameters(arrowType2, tolerateUnknowns);
        
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        boolean tolerateUnknowns = false;
        
        boolean result = arrowType1.checkArrowEquivalenceHelper(arrowType2, tolerateUnknowns);
        
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        int result = arrowType.hashCode();
        
        Assert.assertNotNull(result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        JSType that = new JSType(registry);
        
        JSType result = arrowType.getLeastSupertype(that);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        JSType that = new JSType(registry);
        
        JSType result = arrowType.getGreatestSubtype(that);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        JSType that = new JSType(registry);
        
        TernaryValue result = arrowType.testForEquality(that);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        Visitor<T> visitor = new Visitor<T>();
        
        T result = arrowType.visit(visitor);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        JSType result = arrowType.resolveInternal(t, scope);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        boolean result = arrowType.hasUnknownParamsOrReturn();
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        boolean forAnnotations = false;
        
        String result = arrowType.toStringHelper(forAnnotations);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Node.Kind.PARAMETERS);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        boolean result = arrowType.hasAnyTemplateInternal();
        
        Assert.assertEquals(false, result);
    }
}