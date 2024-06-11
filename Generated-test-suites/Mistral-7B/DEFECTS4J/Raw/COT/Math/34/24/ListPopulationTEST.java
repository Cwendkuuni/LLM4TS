import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new Chromosome() {}));
        Assert.assertEquals(6, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(3);
        population.addChromosomes(Collections.singletonList(new Chromosome() {}));
        population.addChromosomes(Collections.singletonList(new Chromosome() {}));
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(chromosomes);
        Assert.assertEquals(5, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithPopulationLimitExceeded() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        ListPopulation population = new ListPopulation(3);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomes();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomeList();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {});
        Assert.assertEquals(6, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {});
        population.addChromosome(new Chromosome() {});
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return i;
                }
            });
        }
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(chromosomes);
        Chromosome fittest = population.getFittestChromosome();
        Assert.assertEquals(9.0, fittest.getFitness(), 0.001);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitWithPopulationSizeGreaterThanNewPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
        population.setPopulationLimit(3);
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
        String result = population.toString();
        Assert.assertTrue(result.startsWith("["));
        Assert.assertTrue(result.endsWith("]"));
    }
}