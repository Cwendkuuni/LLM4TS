import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

	@Test
	public void testConstructor() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
		assertEquals('\0', hash.getNoEntryValue());
		
		TFloatCharHash hash2 = new TFloatCharHash(10);
		assertEquals(0.0f, hash2.getNoEntryKey(), 0.0001);
		assertEquals('\0', hash2.getNoEntryValue());
		
		TFloatCharHash hash3 = new TFloatCharHash(10, 0.5f);
		assertEquals(0.0f, hash3.getNoEntryKey(), 0.0001);
		assertEquals('\0', hash3.getNoEntryValue());
		
		TFloatCharHash hash4 = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
		assertEquals(1.0f, hash4.getNoEntryKey(), 0.0001);
		assertEquals('a', hash4.getNoEntryValue());
	}
	
	@Test
	public void testSetUp() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(10, hash.setUp(10));
	}
	
	@Test
	public void testContains() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(false, hash.contains(1.0f));
	}
	
	@Test
	public void testForEach() {
		TFloatProcedure procedure = new TFloatProcedure() {
			public boolean execute(float val) {
				return val > 0;
			}
		};
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(true, hash.forEach(procedure));
	}
	
	@Test
	public void testRemoveAt() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.removeAt(0);
	}
	
	@Test
	public void testIndex() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(-1, hash.index(1.0f));
	}
	
	@Test
	public void testIndexRehashed() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte)0));
	}
	
	@Test
	public void testInsertKey() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(0, hash.insertKey(1.0f));
	}
	
	@Test
	public void testInsertKeyRehash() {
		TFloatCharHash hash = new TFloatCharHash();
		try {
			hash.insertKeyRehash(1.0f, 0, 0, (byte)0);
		} catch (IllegalStateException e) {
			assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
		}
	}
	
	@Test
	public void testInsertKeyAt() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.insertKeyAt(0, 1.0f);
	}
	
	@Test
	public void testXinsertKey() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(0, hash.XinsertKey(1.0f));
	}
	
	@Test
	public void testWriteExternal() {
		TFloatCharHash hash = new TFloatCharHash();
		try {
			hash.writeExternal(null);
		} catch (IOException e) {
			fail("IOException should not be thrown");
		}
	}
	
	@Test
	public void testReadExternal() {
		TFloatCharHash hash = new TFloatCharHash();
		try {
			hash.readExternal(null);
		} catch (ClassNotFoundException e) {
			fail("ClassNotFoundException should not be thrown");
		} catch (IOException e) {
			fail("IOException should not be thrown");
		}
	}
}