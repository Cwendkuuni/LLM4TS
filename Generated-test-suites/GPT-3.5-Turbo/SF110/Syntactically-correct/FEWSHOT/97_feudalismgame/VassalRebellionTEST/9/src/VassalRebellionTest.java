package src;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test Case 1
        List<String> col1 = new ArrayList<>();
        col1.add("Fiefdom1");
        VassalRebellion rebellion1 = new VassalRebellion();
        rebellion1.perform(col1);
        // Assert statements

        // Test Case 2
        List<String> col2 = new ArrayList<>();
        col2.add("Fiefdom2");
        VassalRebellion rebellion2 = new VassalRebellion();
        rebellion2.perform(col2);
        // Assert statements

        // Test Case 3
        List<String> col3 = new ArrayList<>();
        col3.add("Fiefdom3");
        VassalRebellion rebellion3 = new VassalRebellion();
        rebellion3.perform(col3);
        // Assert statements

        // Test Case 4
        List<String> col4 = new ArrayList<>();
        col4.add("Fiefdom4");
        VassalRebellion rebellion4 = new VassalRebellion();
        rebellion4.perform(col4);
        // Assert statements

        // Test Case 5
        List<String> col5 = new ArrayList<>();
        col5.add("Fiefdom5");
        VassalRebellion rebellion5 = new VassalRebellion();
        rebellion5.perform(col5);
        // Assert statements
    }
}