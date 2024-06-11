package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import jaw.Main;
import jaw.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

import org.junit.Test;

public class SalvarTest {
    
    @Test
    public void testSalvar() throws Exception {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        List<Entidade> entidade = form.getEntidades();
        assertEquals(entidade, salvar.entidade);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testFiltro2() {
        FileFilter filtro2 = new Salvar().new Filtro2();
        File file1 = new File("test.jaw.xml");
        File file2 = new File("test.xml");
        
        assertEquals(true, filtro2.accept(file1));
        assertEquals(false, filtro2.accept(file2));
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltro() {
        FileFilter filtro = new Salvar().new Filtro();
        File file1 = new File("test.jaw");
        File file2 = new File("test.xml");
        
        assertEquals(true, filtro.accept(file1));
        assertEquals(false, filtro.accept(file2));
        assertEquals("JAW files", filtro.getDescription());
    }
}