package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import jaw.modelo.*;
import jaw.gui.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {
    private ProcessarEntidades form;
    private Salvar salvar;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }
    
    @Test
    public void testSalvarConstructor() {
        assertNotNull(salvar);
        assertEquals(0, salvar.entidade.size());
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testSalvar() {
        String caminhoDosProjetos = Main.configuracao.getCaminhoDosProjetos();
        File filtro2File = new File(caminhoDosProjetos);
        File filtroFile = new File(caminhoDosProjetos);
        
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setCurrentDirectory(filtro2File);
        escolhedor.setFileFilter(new Filtro2());
        escolhedor.showSaveDialog(form);
        assertEquals(true, salvar.vela);
        assertEquals(escolhedor.getSelectedFile().getAbsolutePath(), salvar.arquivo);
        
        escolhedor = new JFileChooser();
        escolhedor.setCurrentDirectory(filtroFile);
        escolhedor.setFileFilter(new Filtro());
        escolhedor.showSaveDialog(form);
        assertEquals(false, salvar.vela);
        
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals(false, salvar.vela);
        
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertEquals(true, salvar.vela);
        
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals(true, salvar.vela);
    }
    
    @Test
    public void testFiltro2Accept() {
        File file1 = new File("test.jaw.xml");
        File file2 = new File("test.xml");
        File file3 = new File("test.jaw");
        
        Filtro2 filtro2 = new Filtro2();
        
        assertTrue(filtro2.accept(file1));
        assertFalse(filtro2.accept(file2));
        assertFalse(filtro2.accept(file3));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Filtro2 filtro2 = new Filtro2();
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAccept() {
        File file1 = new File("test.jaw");
        File file2 = new File("test.xml");
        File file3 = new File("test.jaw.xml");
        
        Filtro filtro = new Filtro();
        
        assertTrue(filtro.accept(file1));
        assertFalse(filtro.accept(file2));
        assertFalse(filtro.accept(file3));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Filtro filtro = new Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
}