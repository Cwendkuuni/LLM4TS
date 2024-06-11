
package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

public class SearchableTextPaneTest {
    
    private SearchableTextPane textPane;
    private Font font;
    private int tabSize;
    
    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
        
        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollPane);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean tracksViewportWidth = textPane.getScrollableTracksViewportWidth();
        // Add assertion here
    }
    
    @Test
    public void testHighlight() {
        String term = "example";
        int flags = 0;
        // Add assertion here
        
        term = null;
        flags = 0;
        // Add assertion here
        
        term = "test";
        flags = 1;
        // Add assertion here
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        int start = 0;
        int length = 5;
        // Add assertion here
        
        start = 5;
        length = 10;
        // Add assertion here
    }
    
    @Test
    public void testRemoveHighlights() {
        // Add assertion here
    }
    
    @Test
    public void testSearch() {
        String term = "example";
        int startPos = 0;
        // Add assertion here
        
        term = null;
        startPos = 0;
        // Add assertion here
        
        term = "test";
        startPos = 1;
        // Add assertion here
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        // Add assertion here
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        // Add assertion here
    }
}
