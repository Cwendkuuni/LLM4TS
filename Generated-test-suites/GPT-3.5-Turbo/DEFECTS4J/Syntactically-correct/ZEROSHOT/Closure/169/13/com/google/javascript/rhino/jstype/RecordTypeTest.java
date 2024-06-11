import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {
@Test
public void testIsSynthetic() {
	RecordType recordType = new RecordType(null, null, false);
	assertEquals(false, recordType.isSynthetic());
}

@Test
public void testCheckRecordEquivalenceHelper() {
	RecordType recordType = new RecordType(null, null, false);
	RecordType otherRecord = new RecordType(null, null, false);
	assertEquals(true, recordType.checkRecordEquivalenceHelper(otherRecord, true));
}

@Test
public void testGetImplicitPrototype() {
	RecordType recordType = new RecordType(null, null, false);
	assertEquals(null, recordType.getImplicitPrototype());
}

@Test
public void testDefineProperty() {
	RecordType recordType = new RecordType(null, null, false);
	assertEquals(false, recordType.defineProperty(null, null, false, null));
}

@Test
public void testGetGreatestSubtypeHelper() {
	RecordType recordType = new RecordType(null, null, false);
	JSType that = null;
	assertEquals(null, recordType.getGreatestSubtypeHelper(that));
}

@Test
public void testToMaybeRecordType() {
	RecordType recordType = new RecordType(null, null, false);
	assertEquals(recordType, recordType.toMaybeRecordType());
}

@Test
public void testIsSubtype() {
	RecordType recordType = new RecordType(null, null, false);
	JSType that = null;
	assertEquals(false, recordType.isSubtype(that));
}

@Test
public void testResolveInternal() {
	RecordType recordType = new RecordType(null, null, false);
	ErrorReporter t = null;
	StaticScope<JSType> scope = null;
	assertEquals(null, recordType.resolveInternal(t, scope));
}

}