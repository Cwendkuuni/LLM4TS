package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import geo.google.Waypoint;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress ret = functor.execute(addr);
        assertNotNull(ret);
        
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        country.setCountryNameCode(countryNames);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getCountry());
        
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryNames.add(countryName);
        countryName.setContent("US");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("US", ret.getCountry());
        
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        adminArea.setAdministrativeAreaName(names);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getState());
        
        AdministrativeArea.AdministrativeAreaName name = new AdministrativeArea.AdministrativeAreaName();
        names.add(name);
        name.setContent("California");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("California", ret.getState());
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getCounty());
        
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        subAdminArea.setSubAdministrativeAreaName(subNames);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getCounty());
        
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subNames.add(subName);
        subName.setContent("County");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("County", ret.getCounty());
        
        Locality locality = new Locality();
        subAdminArea.setLocality(locality);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getCity());
        
        List<Locality.LocalityName> localNames = new ArrayList<>();
        locality.setLocalityName(localNames);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getCity());
        
        Locality.LocalityName localName = new Locality.LocalityName();
        localNames.add(localName);
        localName.setContent("San Francisco");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("San Francisco", ret.getCity());
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getPostalCode());
        
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        postalCode.setPostalCodeNumber(numbers);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getPostalCode());
        
        PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
        numbers.add(number);
        number.setContent("94101");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("94101", ret.getPostalCode());
        
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        thoroughfare.setThoroughfareName(thoroughfares);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        
        ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
        thoroughfares.add(thoroughfare1);
        thoroughfare1.setContent("Main St");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("Main St", ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfares.add(thoroughfare2);
        thoroughfare2.setContent("Apt 102");
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertEquals("Main St", ret.getAddressLine1());
        assertEquals("Apt 102", ret.getAddressLine2());
        
        Waypoint coordinate = new Waypoint();
        addr.setCoordinate(coordinate);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNotNull(ret.getCoordinate());
        
        double[] coordinates = {37.7749, -122.4194};
        coordinate.setCoordinates(coordinates);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNotNull(ret.getCoordinate());
        assertEquals(37.7749, ret.getCoordinate().getLatitude(), 0.0);
        assertEquals(-122.4194, ret.getCoordinate().getLongitude(), 0.0);
        
        ret = functor.execute(addr);
        assertNotNull(ret);
        assertNotNull(ret.getCoordinate());
        assertNull(ret.getAccuracy());
    }
}