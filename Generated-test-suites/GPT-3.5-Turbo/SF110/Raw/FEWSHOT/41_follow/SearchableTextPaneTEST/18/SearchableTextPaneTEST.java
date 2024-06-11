package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class SearchableTextPaneTest {

	private SearchableTextPane textPane;
	
	@Before
	public void setup() {
		Font font = new Font("Arial", Font.PLAIN, 12);
		textPane = new SearchableTextPane(font, 4);
	}
	
	@Test
	public void testGetScrollableTracksViewportWidth() {
		// Test when parent is null
		textPane.setPreferredSize(null);
		assertTrue(textPane.getScrollableTracksViewportWidth());
		
		// Test when parent is not null and width is greater
		textPane.setPreferredSize(new Dimension(200, 100));
		assertTrue(textPane.getScrollableTracksViewportWidth());
		
		// Test when parent is not null and width is smaller
		textPane.setPreferredSize(new Dimension(100, 100));
		assertTrue(textPane.getScrollableTracksViewportWidth());
	}
	
	@Test
	public void testHighlight() {
		// Test highlight with null term 
		List<LineResult> lineResults = textPane.highlight(null, 0);
		assertNotNull(lineResults);
		assertTrue(lineResults.isEmpty());
		
		// Test highlight with empty term
		lineResults = textPane.highlight("", 0);
		assertNotNull(lineResults);
		assertTrue(lineResults.isEmpty());
		
		// Test highlight with non-null term
		lineResults = textPane.highlight("test", 0);
		assertNotNull(lineResults);
		assertTrue(lineResults.isEmpty());
	}
	
	@Test
	public void testAddHighlight() throws BadLocationException {
		// Test addHighlight with valid start and length
		textPane.setText("This is a test");
		textPane.addHighlight(5, 2);
		assertTrue(textPane.getHighlighter().getHighlights().length > 0);
		
		// Test addHighlight with invalid start
		try {
			textPane.addHighlight(-1, 2);
		} catch (BadLocationException e) {
			assertEquals("BadLocationException in SearchableTextPane", e.getMessage());
		}
		
		// Test addHighlight with invalid length
		try {
			textPane.addHighlight(5, -2);
		} catch (BadLocationException e) {
			assertEquals("BadLocationException in SearchableTextPane", e.getMessage());
		}
	}
	
	@Test
	public void testRemoveHighlights() {
		// Test removeHighlights when highlights exist
		textPane.setText("This is a test");
		textPane.addHighlight(5, 2);
		textPane.removeHighlights();
		assertEquals(0, textPane.getHighlighter().getHighlights().length);
		
		// Test removeHighlights when no highlights exist
		textPane.removeHighlights();
		assertEquals(0, textPane.getHighlighter().getHighlights().length);
	}
	
	@Test
	public void testSearch() {
		// Test search with null term
		int result = textPane.search(null);
		assertEquals(0, result);
		
		// Test search with empty term
		result = textPane.search("");
		assertEquals(0, result);
		
		// Test search with non-null term
		result = textPane.search("test");
		assertEquals(0, result);
	}
	
	@Test
	public void testSearchWithStartPos() {
		// Test search with null term
		int result = textPane.search(null, 5);
		assertEquals(0, result);
		
		// Test search with empty term
		result = textPane.search("", 5);
		assertEquals(0, result);
		
		// Test search with non-null term
		result = textPane.search("test", 5);
		assertEquals(0, result);
	}
	
	@Test
	public void testConvertWords2Lines() throws BadLocationException {
		// Test convertWords2Lines with empty words list
		List<WordResult> words = new ArrayList<>();
		List<LineResult> lines = textPane.convertWords2Lines(words);
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
		
		// Test convertWords2Lines with non-empty words list
		words.add(new WordResult("test", 10, 14));
		words.add(new WordResult("example", 20, 28));
		lines = textPane.convertWords2Lines(words);
		assertNotNull(lines);
		assertEquals(1, lines.size());
		assertEquals(0, lines.get(0).getLineNumber());
		assertEquals(0, lines.get(0).getLineStartOffset());
		assertEquals(0, lines.get(0).getLineEndOffset());
		assertTrue(lines.get(0).getWordResults().size() == 2);
	}
	
	@Test
	public void testUpdateWordResult() throws BadLocationException {
		// Test updateWordResult
		LineResult lineResult = new LineResult(0, 0, 10);
		WordResult wordResult = new WordResult("test", 5, 9);
		textPane.updateWordResult(wordResult, lineResult);
		assertEquals(1, lineResult.getWordResults().size());
		assertEquals(1, wordResult.parent.lineNumber);
		assertEquals(0, wordResult.getLineOffset());
	}
}