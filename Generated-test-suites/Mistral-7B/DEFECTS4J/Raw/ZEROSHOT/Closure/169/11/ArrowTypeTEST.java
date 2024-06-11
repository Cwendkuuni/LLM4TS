import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

 private ErrorReporter errorReporter;
 private JSTypeRegistry typeRegistry;

 @BeforeClass
 public static void beforeClass() {
 // Initialize the error reporter and type registry
 errorReporter = new ErrorReporter();
 typeRegistry = new JSTypeRegistry(errorReporter);
 }

 @AfterClass
 public static void afterClass() {
 // Clean up the error reporter and type registry
 errorReporter = null;
 typeRegistry = null;
 }

 @Before
 public void setUp() {
 // Initialize the error reporter and type registry for each test
 errorReporter = new ErrorReporter();
 typeRegistry = new JSTypeRegistry(errorReporter);
 }

 @After
 public void tearDown() {
 // Clean up the error reporter and type registry for each test
 errorReporter = null;
 typeRegistry = null;
 }

 @Test
 public void testArrowTypeCreation() {
 // Test creating an ArrowType with no parameters and a known return type
 Node parametersNode = NodeOperations.createNull();
 Node returnTypeNode = NodeOperations.createIntLiteral(1);
 ArrowType arrowType = new ArrowType(typeRegistry, parametersNode,
 returnTypeNode, false);
 assertEquals("Expected [ArrowType], got: " + arrowType,
 "[ArrowType]", arrowType.toString());

 // Test creating an ArrowType with no parameters and an inferred return type
 ArrowType arrowTypeInferred = new ArrowType(typeRegistry, parametersNode,
 null, true);
 assertEquals("Expected [ArrowType], got: " + arrowTypeInferred,
 "[ArrowType]", arrowTypeInferred.toString());

 // Test creating an ArrowType with parameters and a known return type
 Node param1Node = NodeOperations.createIdentifier("param1");
 Node param2Node = NodeOperations.createIdentifier("param2");
 Node paramListNode = NodeOperations.createList(
 param1Node, param2Node, NodeOperations.createNull());
 ArrowType arrowTypeWithParams = new ArrowType(typeRegistry, paramListNode,
 returnTypeNode, false);
 assertEquals("Expected [ArrowType], got: " + arrowTypeWithParams,
 "[ArrowType]", arrowTypeWithParams.toString());

 // Test creating an ArrowType with parameters and an inferred return type
 ArrowType arrowTypeWithParamsInferred = new ArrowType(typeRegistry, paramListNode,
 null, true);
 assertEquals("Expected [ArrowType], got: " + arrowTypeWithParamsInferred,
 "[ArrowType]", arrowTypeWithParamsInferred.toString());
 }

 @Test
 public void testArrowTypeIsSubtype() {
 // Test that an ArrowType with no parameters and a known return type is a
 // subtype of itself
 ArrowType arrowType1 = new ArrowType(typeRegistry, NodeOperations.createNull(),
 NodeOperations.createIntLiteral(1), false);
 assertTrue("Expected true, got: " + arrowType1.isSubtype(arrowType1),
 arrowType1.isSubtype(arrowType1));

 // Test that an ArrowType with no parameters and a known return type is a
 // subtype of an ArrowType with the same return type but no parameters
 ArrowType arrowType2 = new ArrowType(typeRegistry, NodeOperations.createNull(),
 NodeOperations.createIntLiteral(1), false);
 assertTrue("Expected true, got: " + arrowType1.isSubtype(arrowType2),
 arrowType1.isSubtype(arrowType2));

 // Test that an ArrowType with no parameters and a known return type is not a
 // subtype of an ArrowType with different parameters and the same return type
 ArrowType arrowType3 = new ArrowType(typeRegistry, NodeOperations.createNull(),
 NodeOperations.createIntLiteral(2), false);
 assertFalse("Expected false, got: " + arrowType1.isSubtype(arrowType3),
 arrowType1.isSubtype(arrowType3));

 // Test that an ArrowType with parameters and a known return type is a
 // subtype of itself
 ArrowType arrowTypeWithParams1 = new ArrowType(typeRegistry,
 NodeOperations.createList(NodeOperations.createIdentifier("param1"),
 NodeOperations.createNull()), NodeOperations.createIntLiteral(1), false);
 assertTrue("Expected true, got: " + arrowTypeWithParams1.isSubtype(arrowTypeWithParams1),
 arrowTypeWithParams1.isSubtype(arrowTypeWithParams1));

 // Test that an ArrowType with parameters and a known return type is a
 // subtype of an ArrowType with the same parameters and return type
 ArrowType arrowTypeWithParams2 = new ArrowType(typeRegistry,
 NodeOperations.createList(NodeOperations.createIdentifier("param1"),
 NodeOperations.createNull()), NodeOperations.createIntLiteral(1), false);
 assertTrue("Expected true, got: " + arrowTypeWithParams1.isSubtype(arrowTypeWithParams2),
 arrowTypeWithParams1.isSubtype(arrowTypeWithParams2));

 // Test that an ArrowType with parameters and a known return type is not a
 // subtype of an ArrowType with different parameters and the same return type
 ArrowType arrowTypeWithParams3 = new ArrowType(typeRegistry,
 NodeOperations.createList(NodeOperations.createIdentifier("param2"),
 NodeOperations.createNull()), NodeOperations.createIntLiteral(1), false);
 assertFalse("Expected false, got: " + arrowTypeWithParams1.isSubtype(arrowTypeWithParams3),
 arrowTypeWithParams1.isSubtype(arrowTypeWithParams3));

 // Test that an ArrowType with parameters and an inferred return type is a
 // subtype of itself
 ArrowType arrowTypeWithParamsInferred1 = new ArrowType(typeRegistry,
 NodeOperations.createList(NodeOperations.createIdentifier("param1"),
 NodeOperations.createNull()), null, true);
 assertTrue("Expected true, got: " + arrowTypeWithParamsInferred1.isSubtype(arrowTypeWithParamsInferred1),
 arrowTypeWithParamsInferred1.isSubtype(arrowTypeWithParamsInferred1));

 // Test that an ArrowType with parameters and an inferred return type is a
 // subtype of an ArrowType with the same parameters and an inferred return type
 ArrowType arrowTypeWithParamsInferred2 = new ArrowType(typeRegistry,
 NodeOperations.createList(NodeOperations.createIdentifier("param1"),
 NodeOperations.createNull()), null, true);
 assertTrue("Expected true, got: " + arrowTypeWithParamsInferred1.isSubtype(arrowTypeWithParamsInferred2),
 arrowTypeWithParamsInferred1.isSubtype(arrowTypeWithParamsInferred2));

 // Test that an ArrowType with parameters and an inferred return type is not a
 // subtype of an ArrowType with different parameters and the same inferred return type
 ArrowType arrowTypeWithParamsInferred3 = new ArrowType(typeRegistry,
 NodeOperations.createList(NodeOperations.createIdentifier("param2"),
 NodeOperations.createNull()), null, true);
 assertFalse("Expected false, got: " + arrowTypeWithParamsInferred1.isSubtype(arrowTypeWithParamsInferred3),
 arrowTypeWithParamsInferred1.isSubtype(arrowTypeWithParamsInferred3));
 }

 @Test
 public void testArrowTypeHasEqualParameters() {
 // Test that two ArrowTypes with the same parameters are considered equal
 ArrowType arrowType1 = new ArrowType(typeRegistry, NodeOperations.createList(
 NodeOperations.createIdentifier("param1"), NodeOperations.createNull()),
 NodeOperations.createIntLiteral(1), false);
 ArrowType arrowType2 = new ArrowType(typeRegistry, NodeOperations.createList(
 NodeOperations.createIdentifier("param1"), NodeOperations.createNull()),
 NodeOperations.createIntLiteral(1), false);
 assertTrue("Expected true, got: " + arrowType1.hasEqualParameters(arrowType2, true),
 arrowType1.hasEqualParameters(arrowType2, true));

 // Test that two ArrowTypes with different parameters are not considered equal
 ArrowType arrowType3 = new ArrowType(typeRegistry, NodeOperations.createList(
 NodeOperations.createIdentifier("param1"), NodeOperations.createNull()),
 NodeOperations.createIntLiteral(1), false);
 ArrowType arrowType4 = new ArrowType(typeRegistry, NodeOperations.createList(
 NodeOperations.createIdentifier("param2"), NodeOperations.createNull()),
 NodeOperations.createIntLiteral(1), false);
 assertFalse("Expected false, got: " + arrowType1.hasEqualParameters(arrowType4, true),
 arrowType1.hasEqualParameters(arrowType4, true));
 }
}