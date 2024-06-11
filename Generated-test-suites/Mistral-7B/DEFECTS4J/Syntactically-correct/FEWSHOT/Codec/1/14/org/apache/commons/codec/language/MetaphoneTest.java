package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Basic test cases
        assertEquals("0", metaphone.metaphone("the"));
        assertEquals("TH", metaphone.metaphone("this"));
        assertEquals("K", metaphone.metaphone("cat"));
        assertEquals("KT", metaphone.metaphone("cut"));
        assertEquals("SH", metaphone.metaphone("should"));
        assertEquals("S", metaphone.metaphone("is"));
        assertEquals("S", metaphone.metaphone("isn't"));
        assertEquals("S", metaphone.metaphone("it's"));
        assertEquals("KN", metaphone.metaphone("know"));
        assertEquals("KN", metaphone.metaphone("known"));
        assertEquals("S", metaphone.metaphone("a"));
        assertEquals("S", metaphone.metaphone("an"));
        assertEquals("S", metaphone.metaphone("and"));
        assertEquals("F", metaphone.metaphone("of"));
        assertEquals("F", metaphone.metaphone("for"));
        assertEquals("S", metaphone.metaphone("are"));
        assertEquals("S", metaphone.metaphone("arena"));
        assertEquals("S", metaphone.metaphone("as"));
        assertEquals("S", metaphone.metaphone("about"));
        assertEquals("X", metaphone.metaphone("cie"));
        assertEquals("S", metaphone.metaphone("ci"));
        assertEquals("S", metaphone.metaphone("cy"));

        // Test cases with initials
        assertEquals("S", metaphone.metaphone("scot"));
        assertEquals("K", metaphone.metaphone("knew"));
        assertEquals("S", metaphone.metaphone("scion"));
        assertEquals("C", metaphone.metaphone("cent"));
        assertEquals("X", metaphone.metaphone("ciao"));

        // Test cases with double letters
        assertEquals("S", metaphon.metaphone("apple"));
        assertEquals("S", metaphone.metaphone("bottle"));
        assertEquals("S", metaphone.metaphone("bucket"));
        assertEquals("S", metaphone.metaphone("cannon"));
        assertEquals("S", metaphone.metaphone("dollard"));
        assertEquals("S", metaphone.metaphone("mansion"));
        assertEquals("S", metaphone.metaphone("promise"));
        assertEquals("S", metaphone.metaphone("station"));

        // Test cases with prefixes
        assertEquals("T", metaphone.metaphone("theater"));
        assertEquals("T", metaphone.metaphone("theatre"));
        assertEquals("T", metaphone.metaphone("thisstrument"));
        assertEquals("T", metaphone.metaphone("theist"));
        assertEquals("S", metaphone.metaphone("island"));
        assertEquals("S", metaphone.metaphone("islet"));
        assertEquals("S", metaphone.metaphone("isolation"));
        assertEquals("S", metaphone.metaphone("isolate"));

        // Test cases with suffixes
        assertEquals("S", metaphone.metaphone("basketball"));
        assertEquals("S", metaphone.metaphone("football"));
        assertEquals("S", metaphone.metaphone("baseball"));
        assertEquals("S", metaphone.metaphone("swimming"));
        assertEquals("S", metaphone.metaphone("racing"));

        // Test cases with silent letters
        assertEquals("J", metaphone.metaphone("bough"));
        assertEquals("J", metaphone.metaphone("thought"));
        assertEquals("J", metaphone.metaphone("through"));
        assertEquals("J", metaphone.metaphone("bought"));
        assertEquals("J", metaphone.metaphone("nothing"));
        assertEquals("J", metaphone.metaphone("because"));

        // Test cases with consonant clusters
        assertEquals("SK", metaphone.metaphone("asking"));
        assertEquals("SK", metaphone.metaphone("asked"));
        assertEquals("SK", metaphone.metaphone("askington"));
        assertEquals("SK", metaphone.metaphone("askance"));
        assertEquals("SK", metaphone.metaphone("askapart"));
        assertEquals("SK", metaphone.metaphone("askance"));
        assertEquals("SK", metaphone.metaphone("askingful"));
        assertEquals("SK", metaphone.metaphone("asketball"));
        assertEquals("SK", metaphone.metaphone("askpet"));

        // Test cases with vowel combinations
        assertEquals("S", metaphone.metaphone("eighth"));
        assertEquals("S", metaphone.metaphone("eighths"));
        assertEquals("S", metaphone.metaphone("eighthly"));
        assertEquals("S", metaphone.metaphone("eighteen"));
        assertEquals("S", metaphone.metaphone("eighteenth"));
        assertEquals("S", metaphone.metaphone("eighths"));
        assertEquals("S", metaphone.metaphone("eighths"));
        assertEquals("S", metaphone.metaphone("eighth"));
        assertEquals("S", metaphone.metaphone("eighth"));
        assertEquals("S", metaphone.metaphone("eighths"));
        assertEquals("S", metaphone.metaphone("eighths"));

        // Test cases with irregular words
        assertEquals("F", metaphone.metaphone("hour"));
        assertEquals("F", metaphone.metaphone("hourly"));
        assertEquals("F", metaphone.metaphone("hourglass"));
        assertEquals("F", metaphone.metaphone("hourlong"));
        assertEquals("F", metaphone.metaphone("hourly"));
        assertEquals("F", metaphone.metaphone("hour"));
        assertEquals("F", metaphone.metaphone("hour"));
        assertEquals("F", metaphone.metaphone("hour"));
        assertEquals("F", metaphone.metaphone("hour"));
        assertEquals("F", metaphone.metaphone("hour"));

        // Test cases with compound words
        assertEquals("S", metaphone.metaphone("applepie"));
        assertEquals("S", metaphone.metaphone("applesauce"));
        assertEquals("S", metaphone.metaphone("blueberry"));
        assertEquals("S", metaphone.metaphone("cherrypie"));
        assertEquals("S", metaphone.metaphone("raspberry"));
        assertEquals("S", metaphone.metaphone("watermelon"));
        assertEquals("S", metaphone.metaphone("pumpkinpie"));
        assertEquals("S", metaphone.metaphone("peachpie"));
        assertEquals("S", metaphone.metaphone("bananana"));
        assertEquals("S", metaphone.metaphone("kiwifruit"));

        // Test cases with contractions
        assertEquals("S", metaphone.metaphone("it's"));
        assertEquals("S", metaphone.metaphone("isnt't"));
        assertEquals("S", metaphone.metaphone("isnt"));
        assertEquals("S", metaphone.metaphone("isnt't"));
        assertEquals("S", metaphone.metaphone("aint"));
        assertEquals("S", metaphone.metaphone("aint't"));
        assertEquals("S", metaphone.metaphone("aint't"));
        assertEquals("S", metaphone.metaphone("aint"));
        assertEquals("S", metaphone.metaphone("aint't"));
        assertEquals("S", metaphone.metaphone("aint"));

        // Test cases with complex words
        assertEquals("S", metaphone.metaphone("unquestionably"));
        assertEquals("S", metaphone.metaphone("unquestionably"));
        assertEquals("S", metaphone.metaphone("unquestioning"));
        assertEquals("S", metaphone.metaphone("unquestioning"));
        assertEquals("S", metaphone.metaphone("unquestioned"));
        assertEquals("S", metaphone.metaphone("unquestioned"));
        assertEquals("S", metaphone.metaphone("unquestioned"));
        assertEquals("S", metaphone.metaphone("unquestioned"));
        assertEquals("S", metaphone.metaphone("unquestioned"));
        assertEquals("S", metaphone.metaphone("unquestioned"));

        // Test cases with very long words
        assertEquals("S", metaphone.metaphone("antidisestablishmentarianism"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarianism"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));
        assertEquals("S", metaphone.metaphone("antidisestablishmentarian"));

        // Test cases with capitalized words
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("Apple"));

        // Test cases with different casing
        assertEquals("S", metaphone.metaphone("Apple"));
        assertEquals("S", metaphone.metaphone("apple"));
        assertEquals("S", metaphone.metaphone("APPLE"));
        assertEquals("S", metaphone.metaphone("APple"));
        assertEquals("S", metaphone.metaphone("aPple"));
        assertEquals("S", metaphone.metaphone("apPlE"));
        assertEquals("S", metaphone.metaphone("applE"));
        assertEquals("S", metaphone.metaphone("applE"));
        assertEquals("S", metaphone.metaphone("applE"));
        assertEquals("S", metaphone.metaphone("applE"));

        // Test cases with numbers and special characters
        assertEquals("S", metaphone.metaphone("Apple1"));
        assertEquals("S", metaphone.metaphone("Apple@"));
        assertEquals("S", metaphone.metaphone("Apple#"));
        assertEquals("S", metaphone.metaphone("Apple$"));
        assertEquals("S", metaphone.metaphone("Apple%"));
        assertEquals("S", metaphone.metaphone("Apple^"));
        assertEquals("S", metaphone.metaphone("Apple&"));
        assertEquals("S", metaphone.metaphone("Apple*"));
        assertEquals("S", metaphone.metaphone("Apple("));
        assertEquals("S", metaphone.metaphone("Apple)"));

        // Test cases with complex combinations of letters, numbers and special characters
        assertEquals("S", metaphone.metaphone("Apple1@"));
        assertEquals("S", metaphone.metaphone("Apple1#"));
        assertEquals("S", metaphone.metaphone("Apple1$"));
        assertEquals("S", metaphone.metaphone("Apple1%"));
        assertEquals("S", metaphone.metaphone("Apple1^"));
        assertEquals("S", metaphone.metaphone("Apple1&"));
        assertEquals("S", metaphone.metaphone("Apple1*"));
        assertEquals("S", metaphone.metaphone("Apple1("));
        assertEquals("S", metaphone.metaphone("Apple1)"));
        assertEquals("S", metaphone.metaphone("Apple1"));

        // Test cases with empty or null strings
        assertEquals("", metaphone.metaphone(""));
        assertEquals("", metaphone.metaphone(null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(new Object() {});
    }
}