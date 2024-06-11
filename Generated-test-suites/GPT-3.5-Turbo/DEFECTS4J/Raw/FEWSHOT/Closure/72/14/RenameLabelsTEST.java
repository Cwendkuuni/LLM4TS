
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

   private RenameLabels.DefaultNameSupplier supplier;
   private RenameLabels classUnderTest;

   @Before
   public void setUp() {
       supplier = new RenameLabels.DefaultNameSupplier();
       classUnderTest = new RenameLabels(new AbstractCompiler());
   }

   @Test
   public void testGetNameForId() {
       assertEquals("a", classUnderTest.getNameForId(1));
       assertEquals("b", classUnderTest.getNameForId(2));
       assertEquals("c", classUnderTest.getNameForId(3));
       assertEquals("d", classUnderTest.getNameForId(4));
       assertEquals("e", classUnderTest.getNameForId(5));
   }

   @Test
   public void testGetLabelInfo() {
       RenameLabels.ProcessLabels processLabels = classUnderTest.new ProcessLabels();
       RenameLabels.LabelNamespace labelNamespace = new RenameLabels.LabelNamespace();
       processLabels.namespaceStack.push(labelNamespace);

       RenameLabels.LabelInfo li1 = new RenameLabels.LabelInfo(1);
       RenameLabels.LabelInfo li2 = new RenameLabels.LabelInfo(2);
       RenameLabels.LabelInfo li3 = new RenameLabels.LabelInfo(3);
       labelNamespace.renameMap.put("label1", li1);
       labelNamespace.renameMap.put("label2", li2);
       labelNamespace.renameMap.put("label3", li3);

       assertEquals(li1, processLabels.getLabelInfo("label1"));
       assertEquals(li2, processLabels.getLabelInfo("label2"));
       assertEquals(li3, processLabels.getLabelInfo("label3"));
   }
}
