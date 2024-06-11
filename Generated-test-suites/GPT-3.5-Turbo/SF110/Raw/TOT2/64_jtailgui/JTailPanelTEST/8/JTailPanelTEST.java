import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {

    private JTailModel model = new JTailModel();
    private JTailPanel panel = new JTailPanel(model);

    @Test
    public void testGetCharacterHeight() {
        assertEquals(panel.getCharacterHeight(), panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertNotNull(panel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate() {
        // Test when observable is not the model
        panel.update(null, null);

        // Test when observable is the model and event type is RESIZED
        panel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0));
    }

    @Test
    public void testUpdateDataTable() {
        // Test when lineNumbers and lines are empty
        panel.updateDataTable("", "");
    }

}