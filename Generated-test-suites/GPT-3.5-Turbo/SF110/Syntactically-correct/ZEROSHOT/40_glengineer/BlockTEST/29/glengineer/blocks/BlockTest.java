import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.blocks.*;

public class BlockTest {

	@Test
	public void testConstructor_WithValidInputs() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme, 0, 0, 1, 1);
		assertEquals(0, block.x1);
		assertEquals(0, block.y1);
		assertEquals(1, block.x2);
		assertEquals(1, block.y2);
		assertEquals(scheme, block.scheme);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WithInvalidInputs() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme, -1, 0, 1, 1);
	}
	
	@Test
	public void testConstructor_WithCharPositions() {
		Scheme scheme = new Scheme();
		CharPosition p1 = new CharPosition(0, 0);
		CharPosition p2 = new CharPosition(1, 1);
		Block block = new ConcreteBlock(scheme, p1, p2);
		assertEquals(0, block.x1);
		assertEquals(0, block.y1);
		assertEquals(1, block.x2);
		assertEquals(1, block.y2);
		assertEquals(scheme, block.scheme);
	}
	
	@Test
	public void testConstructor_WithScheme() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		assertEquals(0, block.x1);
		assertEquals(0, block.y1);
		assertEquals(scheme.x2, block.x2);
		assertEquals(scheme.y2, block.y2);
		assertEquals(scheme, block.scheme);
	}
	
	@Test
	public void testSubblock() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		Block subblock = block.subblock(0, 0, 1, 1);
		assertEquals(0, subblock.x1);
		assertEquals(0, subblock.y1);
		assertEquals(1, subblock.x2);
		assertEquals(1, subblock.y2);
		assertEquals(scheme, subblock.scheme);
	}
	
	@Test
	public void testCharAt_WithValidInputs() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		char character = block.charAt(0, 0);
		assertEquals(scheme.lines[0].charAt(0), character);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCharAt_WithInvalidInputs() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		char character = block.charAt(5, 5);
	}
	
	@Test
	public void testTextAt_WithValidInputs() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		String text = block.textAt(0, 1, 0);
		assertEquals(scheme.lines[0].substring(0, 1), text);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTextAt_WithInvalidInputs() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		String text = block.textAt(10, 20, 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContainsOnlyOneWord_WithTrivialBlock() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		block.containsOnlyOneWord();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContainsOnlyOneWord_WithEmptyBlock() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		block.containsOnlyOneWord();
	}
	
	@Test
	public void testToString() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		String expected = "Block(0,0, " + scheme.x2 + "," + scheme.y2 + "):\n";
		expected += " #############################\n";
		for (int y = block.y1; y < block.y2; y++) {
			expected += " # " + scheme.textAt(block.x1, block.x2, y) + " #\n";
		}
		expected += " #############################";
		assertEquals(expected, block.toString());
	}
	
	@Test
	public void testGetUpperLine() {
		Scheme scheme = new Scheme();
		Block block = new ConcreteBlock(scheme);
		String upperLine = block.getUpperLine();
		assertEquals(scheme.textAt(block.x1, block.x2, block.y1), upperLine);
	}

}

class ConcreteBlock extends Block {
	
	public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
		super(scheme, x1, y1, x2, y2);
	}
	
	@Override
	public Block subblock(int p0, int p1, int p2, int p3) {
		return null;
	}

	@Override
	public List<Block> extractSequentialSubblocks() {
		return null;
	}

	@Override
	public List<Block> extractParallelSubblocks() {
		return null;
	}
}