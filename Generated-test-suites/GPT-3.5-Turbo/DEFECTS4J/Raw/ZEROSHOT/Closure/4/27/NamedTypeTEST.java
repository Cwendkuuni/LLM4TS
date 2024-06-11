import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

	private NamedType namedType;

	@Before
	public void setUp() {
		namedType = new NamedType(null, "reference", "sourceName", 1, 1);
	}

	@Test
	public void testGetReferencedType() {
		JSType referencedType = namedType.getReferencedType();
		assertEquals(referencedType, namedType.getReferencedTypeInternal());
	}

	@Test
	public void testGetReferenceName() {
		String referenceName = namedType.getReferenceName();
		assertEquals("reference", referenceName);
	}

	@Test
	public void testToStringHelper() {
		String stringHelper = namedType.toStringHelper(true);
		assertEquals("reference", stringHelper);
	}

	@Test
	public void testHasReferenceName() {
		assertTrue(namedType.hasReferenceName());
	}

	@Test
	public void testIsNamedType() {
		assertTrue(namedType.isNamedType());
	}

	@Test
	public void testIsNominalType() {
		assertTrue(namedType.isNominalType());
	}

	@Test
	public void testHashCode() {
		int hashCode = namedType.hashCode();
		assertNotNull(hashCode);
	}

	@Test
	public void testResolveInternal() {
		ErrorReporter errorReporter = null;
		StaticScope<JSType> enclosing = null;
		JSType resolvedTypeInternal = namedType.resolveInternal(errorReporter, enclosing);
		assertNotNull(resolvedTypeInternal);
	}

	@Test
	public void testSetValidator() {
		Predicate<JSType> validator = null;
		boolean set = namedType.setValidator(validator);
		assertTrue(set);
	}

	@Test
	public void testDefineProperty() {
		String propertyName = "propertyName";
		JSType type = null;
		boolean inferred = false;
		Node propertyNode = null;
		boolean defineProperty = namedType.defineProperty(propertyName, type, inferred, propertyNode);
		assertTrue(defineProperty);
	}

}