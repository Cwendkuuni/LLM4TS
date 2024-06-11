import org.junit.Test;

public class RuntimeTypeCheckTest {

    @Test
    public void testAddMarkers() {
        // Test Case 1: Verify that the marker is added for a class C.
        // Create a new RuntimeTypeCheck instance
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck();
        
        // Create a mock function node for class C
        Node constructorNode = new Node();
        constructorNode.setJSType(new FunctionType());
        constructorNode.getJSType().setConstructor(true);
        
        // Call the private method "AddMarkers.visitFunction()" with the mocked inputs
        AddMarkers addMarkers = runtimeTypeCheck.new AddMarkers();
        addMarkers.visitFunction(null, constructorNode);
        
        // Verify that the marker property C.prototype['instance_of__C'] is added
        assertTrue(NodeUtil.hasMarker(constructorNode, "instance_of__C"));
        
        // Test Case 2: Verify that the marker is added for each implemented interface I.
        ObjectType interfaceType1 = new ObjectType();
        interfaceType1.setReferenceName("I1");
        
        ObjectType interfaceType2 = new ObjectType();
        interfaceType2.setReferenceName("I2");
        
        // Add the interface types to the function type
        constructorNode.getJSType().setAllImplementedInterfaces(Arrays.asList(interfaceType1, interfaceType2));
        
        // Call the private method "AddMarkers.visitFunction()" with the mocked inputs
        addMarkers.visitFunction(null, constructorNode);
        
        // Verify that the marker properties C.prototype['implements__I1'] and C.prototype['implements__I2'] are added
        assertTrue(NodeUtil.hasMarker(constructorNode, "implements__I1"));
        assertTrue(NodeUtil.hasMarker(constructorNode, "implements__I2"));
    }

    @Test
    public void testAddChecks() {
        // Test Case 1: Verify that type checks are added for function parameters.
        // Create a new RuntimeTypeCheck instance
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck();
        
        // Create a mock function node with 2 parameters and a return type
        Node functionNode = new Node();
        functionNode.setJSType(new FunctionType());

        JSType paramType1 = new JSType();
        JSType paramType2 = new JSType();
        
        // Set the parameter types and return type
        functionNode.getJSType().setParameters(Arrays.asList(paramType1, paramType2));
        functionNode.getJSType().setReturnType(new JSType());
        
        // Call the private method "AddChecks.visitFunction()" with the mocked inputs
        AddChecks addChecks = runtimeTypeCheck.new AddChecks();
        addChecks.visitFunction(null, functionNode);
        
        // Verify that type checks are added for each parameter and the return type
        assertTrue(NodeUtil.hasCheckTypeCall(functionNode, paramType1));
        assertTrue(NodeUtil.hasCheckTypeCall(functionNode, paramType2));
        assertTrue(NodeUtil.hasCheckTypeCall(functionNode, functionNode.getJSType().getReturnType()));

        // Test Case 2: Verify that type check is added for the return value.
        // Create a new RuntimeTypeCheck instance
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck();
        
        // Create a mock function node with a return value
        Node functionNode = new Node();
        functionNode.setJSType(new FunctionType());
        
        JSType returnType = new JSType();
        
        // Set the return type
        functionNode.getJSType().setReturnType(returnType);
        
        // Call the private method "AddChecks.visitReturn()" with the mocked inputs
        AddChecks addChecks = runtimeTypeCheck.new AddChecks();
        addChecks.visitReturn(null, functionNode);
        
        // Verify that a type check is added for the return value
        assertTrue(NodeUtil.hasCheckTypeCall(functionNode, returnType));
    }

    @Test
    public void testGetBoilerplateCode() {
        // Test Case 1: Verify that the boilerplate code is correctly retrieved.
        // Create a new RuntimeTypeCheck instance
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck();
        
        // Call the private method "getBoilerplateCode()" with a mock compiler and logFunction
        Node boilerplateCode = runtimeTypeCheck.getBoilerplateCode(new AbstractCompiler(), null);
        
        // Verify that the boilerplate code is not null and has the expected structure
        assertNotNull(boilerplateCode);
        assertEquals("jscomp.typecheck.checkType", boilerplateCode.getFirstChild().getLastChild().getString());
    }
}