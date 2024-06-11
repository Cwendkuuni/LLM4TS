import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.testing.BaseTestCase;
import com.google.javascript.rhino.testing.PolymerTestRunner;

public class NamedTypeTest extends BaseTestCase {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;
 private StaticScope<JSType> enclosing;

 @Override
 public void setUp() throws Exception {
 super.setUp();

 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter();
 enclosing = new StaticScope<JSType>(registry);
 }

 /**
 * Test constructor with valid parameters.
 */
 public void testNamedTypeWithValidParameters() {
 String reference = "TypeA";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;
 JSType type = new JSTypeRegistry(registry).getType("TypeB");
 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 assertEquals("TypeA", namedType.getReferenceName());
 assertEquals(type, namedType.getReferencedType());
 }

 /**
 * Test constructor with null reference.
 */
 public void testNamedTypeWithNullReference() {
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;
 try {
 NamedType namedType = new NamedType(registry, null, sourceName, lineno, charno);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 /**
 * Test defineProperty method with unresolved type.
 */
 public void testDefinePropertyWithUnresolvedType() {
 String reference = "TypeA";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;
 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

 StaticSlot<JSType> slot = enclosing.put("TypeB", null);
 String propertyName = "property";
 JSType propertyType = registry.getNativeFunctionType(JSTypeNative.FUNCTION_TYPE);
 Node propertyNode = createNode("function() {}");

 namedType.defineProperty(propertyName, propertyType, false, propertyNode);
 assertNull(namedType.getReferencedType());
 }

 /**
 * Test resolveInternal method with valid type from registry.
 */
 public void testResolveInternalWithValidTypeFromRegistry() {
 String reference = "TypeA";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;
 JSType type = new JSTypeRegistry(registry).getType("TypeB");
 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

 registry.put("TypeB", type);
 JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
 assertEquals(type, referencedType);
 }

 /**
 * Test resolveInternal method with unresolved type via properties.
 */
 public void testResolveInternalWithUnresolvedTypeViaProperties() {
 String reference = "TypeA.TypeB";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;

 StaticScope<JSType> enclosingStaticScope = new StaticScope<JSType>(registry);
 enclosingStaticScope.put("TypeA", new ObjectType(registry));
 StaticSlot<JSType> typeA = enclosingStaticScope.getSlot("TypeA");
 typeA.setType(new ObjectType(registry));

 ObjectType typeA = ObjectType.cast(typeA.getType());
 typeA.defineProperty("TypeB", registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), false, null);

 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), referencedType);
 }

 /**
 * Test resolveInternal method with cyclic inheritance.
 */
 public void testResolveInternalWithCyclicInheritance() {
 String reference = "TypeA.TypeB";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;

 StaticScope<JSType> enclosingStaticScope = new StaticScope<JSType>(registry);
 enclosingStaticScope.put("TypeA", new ObjectType(registry));
 StaticSlot<JSType> typeA = enclosingStaticScope.getSlot("TypeA");
 typeA.setType(new ObjectType(registry));

 ObjectType typeA = ObjectType.cast(typeA.getType());
 typeA.defineProperty("TypeB", new NamedType(registry, "TypeA.TypeB", sourceName, lineno, charno), false, null);
 typeA.defineProperty("TypeB", typeA, false, null);

 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
 assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), referencedType);
 }

 /**
 * Test resolveInternal method with forward-declared type.
 */
 public void testResolveInternalWithForwardDeclaredType() {
 String reference = "TypeA";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;

 registry.setForwardDeclaredType(reference, true);
 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
 assertNull(referencedType);
 }

 /**
 * Test detectImplicitPrototypeCycle method with unresolved type.
 */
 public void testDetectImplicitPrototypeCycleWithUnresolvedType() {
 String reference = "TypeA.TypeB";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;

 ObjectType typeA = new ObjectType(registry);
 typeA.defineProperty("TypeB", new NamedType(registry, reference, sourceName, lineno, charno), false, null);
 typeA.defineProperty("TypeB", typeA, true, null); // set prototype

 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 namedType.resolveInternal(errorReporter, enclosing);
 assertTrue(namedType.detectImplicitPrototypeCycle());
 }

 /**
 * Test detectImplicitPrototypeCycle method with resolved type.
 */
 public void testDetectImplicitPrototypeCycleWithResolvedType() {
 String reference = "TypeA.TypeB";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;

 ObjectType typeA = new ObjectType(registry);
 typeA.defineProperty("TypeB", registry.getType("TypeC"), false, null);

 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 namedType.resolveInternal(errorReporter, enclosing);
 assertFalse(namedType.detectImplicitPrototypeCycle());
 }

 /**
 * Test isResolved method with unresolved type.
 */
 public void testIsResolvedWithUnresolvedType() {
 String reference = "TypeA";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;
 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 assertFalse(namedType.isResolved());
 }

 /**
 * Test isResolved method with resolved type.
 */
 public void testIsResolvedWithResolvedType() {
 String reference = "TypeA";
 String sourceName = "testSourceName";
 int lineno = 1;
 int charno = 1;
 JSType type = new JSTypeRegistry(registry).getType("TypeB");
 NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
 registry.put("TypeB", type);
 namedType.resolveInternal(errorReporter, enclosing);
 assertTrue(namedType.isResolved());
 }

 @After
 public void tearDown() throws Exception {
 registry = null;
 errorReporter = null;
 enclosing = null;
 super.tearDown();
 }

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 PolymerTestRunner.setUpBeforeClass();
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 PolymerTestRunner.tearDownAfterClass();
 }
}