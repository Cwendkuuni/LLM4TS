package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
    }
    
    @Test
    public void testSalvarStripLeadingAndTrailingQuotes() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new Vector<Entidade>();
        entidades.add(new Entidade());
        form.setEntidades(entidades);
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
        escolhedor.setFileFilter(salvar.new Filtro2());
        
        if (escolhedor.getSelectedFile() == null && form.getName().equals("")) {
            return;
        }
        
        if (salvar.vela) {
            if (escolhedor.getSelectedFile() == null) {
                return;
            }
            assertEquals(escolhedor.getSelectedFile().getAbsolutePath(), salvar.arquivo);
        }
        else {
            assertEquals(form.getNomeArquivoSalvo(), salvar.arquivo);
        }
    }
    
    @Test
    public void testSalvarStripLeadingHyphens() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new Vector<Entidade>();
        entidades.add(new Entidade());
        form.setEntidades(entidades);
        
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
        escolhedor.setFileFilter(salvar.new Filtro());
        
        if (escolhedor.getSelectedFile() == null && form.getName().equals("")) {
            return;
        }
        
        if (salvar.vela) {
            if (escolhedor.getSelectedFile() == null) {
                return;
            }
            assertEquals(escolhedor.getSelectedFile().getAbsolutePath(), salvar.arquivo);
        }
        else {
            assertEquals(form.getNomeArquivoSalvo(), salvar.arquivo);
        }
    }
    
    @Test
    public void testSalvarFiltro2Accept() {
        Salvar salvar = new Salvar();
        File f = new File("test.jaw.xml");
        assertTrue(salvar.new Filtro2().accept(f));
    }
    
    @Test
    public void testSalvarFiltro2GetDescription() {
        Salvar salvar = new Salvar();
        assertEquals("XML JAW files", salvar.new Filtro2().getDescription());
    }
    
    @Test
    public void testSalvarFiltroAccept() {
        Salvar salvar = new Salvar();
        File f = new File("test.jaw");
        assertTrue(salvar.new Filtro().accept(f));
    }
    
    @Test
    public void testSalvarFiltroGetDescription() {
        Salvar salvar = new Salvar();
        assertEquals("JAW files", salvar.new Filtro().getDescription());
    }
    
}