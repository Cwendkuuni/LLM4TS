package ghm.follow.search;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPane extends JTextArea
{
    private Logger log;
    private int lastSearchPos;
    private String lastSearchTerm;
    private final DefaultHighlighter.DefaultHighlightPainter wordPainter;
    
    public SearchableTextPane(final Font font, final int tabSize) {
        this.log = Logger.getLogger(SearchableTextPane.class.getName());
        this.lastSearchPos = -1;
        this.wordPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        this.setFont(font);
        this.setTabSize(tabSize);
    }
    
    @Override
    public boolean getScrollableTracksViewportWidth() {
        final Component parent = this.getParent();
        final ComponentUI ui = this.getUI();
        return parent == null || ui.getPreferredSize(this).width <= parent.getSize().width;
    }
    
    public List<LineResult> highlight(final String term, final int flags) {
        List<LineResult> lineResults = new ArrayList<LineResult>();
        this.removeHighlights();
        if (term != null && term.length() > 0) {
            try {
                final Document doc = this.getDocument();
                final String text = doc.getText(0, doc.getLength());
                final List<WordResult> searchResults = new SearchEngine(flags).search(term, text);
                lineResults = this.convertWords2Lines(searchResults);
                for (final LineResult lineResult : lineResults) {
                    final List<WordResult> wordResults = lineResult.getWordResults();
                    for (final WordResult wordResult : wordResults) {
                        final int wordStart = wordResult.start;
                        final int wordEnd = wordResult.end;
                        this.addHighlight(wordStart, wordEnd - wordStart);
                        Thread.yield();
                    }
                }
            }
            catch (BadLocationException e) {
                this.log.log(Level.SEVERE, "BadLocationException in SearchableTextPane", e);
                lineResults = new ArrayList<LineResult>();
            }
        }
        return lineResults;
    }
    
    private void addHighlight(final int start, final int length) throws BadLocationException {
        this.getHighlighter().addHighlight(start, start + length, this.wordPainter);
    }
    
    public void removeHighlights() {
        this.getHighlighter().removeAllHighlights();
    }
    
    public int search(final String term) {
        if (term != null && term.length() > 0) {
            if (term.equals(this.lastSearchTerm)) {
                int pos = 0;
                if (this.lastSearchPos != -1) {
                    pos = this.lastSearchPos + this.lastSearchTerm.length();
                }
                this.lastSearchPos = this.search(this.lastSearchTerm, pos);
            }
            else {
                this.lastSearchPos = this.search(term, 0);
            }
        }
        if (this.lastSearchPos == -1) {
            this.lastSearchTerm = null;
        }
        else {
            this.lastSearchTerm = term;
        }
        return this.lastSearchPos;
    }
    
    public int search(final String term, final int startPos) {
        int pos = 0;
        try {
            final Document doc = this.getDocument();
            final String text = doc.getText(0, doc.getLength());
            pos = text.indexOf(term, startPos);
        }
        catch (BadLocationException e) {
            this.log.log(Level.WARNING, "BadLocationException in SearchableTextPane", e);
            pos = -1;
        }
        return pos;
    }
    
    private List<LineResult> convertWords2Lines(final List<WordResult> words) throws BadLocationException {
        final ArrayList<LineResult> lines = new ArrayList<LineResult>();
        LineResult tempLine = null;
        int lastLine = -1;
        for (final WordResult word : words) {
            final int line = this.getLineOfOffset(word.start);
            if (line != lastLine) {
                if (tempLine != null) {
                    lines.add(tempLine);
                }
                final Element elem = Utilities.getParagraphElement(this, word.start);
                final int lineStart = elem.getStartOffset();
                final int lineEnd = elem.getEndOffset();
                tempLine = new LineResult(line, lineStart, lineEnd);
            }
            this.updateWordResult(word, tempLine);
            lastLine = line;
            Thread.yield();
        }
        if (tempLine != null) {
            lines.add(tempLine);
        }
        return lines;
    }
    
    private void updateWordResult(final WordResult wordResult, final LineResult lineResult) throws BadLocationException {
        lineResult.addWord(wordResult);
        final int line = this.getLineOfOffset(wordResult.start);
        wordResult.parent.lineNumber = line + 1;
        final int lineOffset = this.getLineStartOffset(line);
        wordResult.setLineOffset(lineOffset);
    }
}
