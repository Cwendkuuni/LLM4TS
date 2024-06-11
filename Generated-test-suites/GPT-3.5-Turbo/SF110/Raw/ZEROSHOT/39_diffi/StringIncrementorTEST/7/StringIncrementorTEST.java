package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    private final String a = "abcdefg";
    private final String b = "xyzdeffg";

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals(1, result.linksCount());
        
        StringLink link = result.getLink(0);
        assertNotNull(link);
        assertEquals(3, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(3, link.getLen());
    }

    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertNotNull(result);
        assertEquals(1, result.linksCount());
        
        StringLink link = result.getLink(0);
        assertNotNull(link);
        assertEquals(3, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(3, link.getLen());
    }

    @Test
    public void testPatch() {
        RelativeString rs = new RelativeString();
        StringLink link = new StringLink(3, 0, 3);
        rs.addLink(link);
        rs.setAbsolute("xyz");
        
        String patchedString = StringIncrementor.patch(a, rs);
        assertEquals(b, patchedString);
    }

    @Test
    public void testPatchVerbose() {
        RelativeString rs = new RelativeString();
        StringLink link = new StringLink(3, 0, 3);
        rs.addLink(link);
        rs.setAbsolute("xyz");
        
        String patchedString = StringIncrementor.patch(a, rs, true);
        assertEquals(b, patchedString);
    }

}